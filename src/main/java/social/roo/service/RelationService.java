package social.roo.service;

import com.blade.ioc.annotation.Bean;
import social.roo.RooConst;
import social.roo.utils.ArrayUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 关系服务
 * <p>
 * 粉丝、关注、收藏、点赞
 *
 * @author biezhi
 * @date 2017/8/2
 */
@Bean
@Slf4j
public class RelationService {

    private DB db = DBMaker.fileDB(RooConst.RELATE_DBNAME).fileMmapEnable().make();

    public RelationService() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            db.close();
            log.info("closed mapdb");
        }));
    }

    /**
     * 获取一个用户的所有粉丝id列表
     *
     * @param uid
     * @return 返回用户id列表
     */
    public List<Long> getFollowers(Long uid) {
        Map<Long, long[]> map = db.treeMap(RooConst.DBKEY_FOLLOW)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        return this.getIds(map.get(uid));
    }

    /**
     * 获取一个用户的所有关注
     *
     * @param uid
     * @return 返回用户id列表
     */
    public List<Long> getFollowing(Long uid) {
        Map<String, long[]> map = db.treeMap(RooConst.DBKEY_FOLLOWING)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        return this.getIds(map.get(uid));
    }

    /**
     * 获取一个用户收藏的所有帖子
     *
     * @param uid
     * @return 返回帖子id列表
     */
    public List<String> getUserFavorites(Long uid) {
        Map<Long, long[]> map = db.treeMap(RooConst.DBKEY_USER_FAVORITES)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        List<Long> longs = this.getIds(map.get(uid));
        if (null != longs && !longs.isEmpty()) {
            return longs.stream()
                    .map(id -> Long.toString(id, 36))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取一个帖子的所有like人员列表
     *
     * @param tid
     * @return 返回uid列表
     */
    public List<Long> getLikes(String tid) {
        Map<String, long[]> map = db.treeMap(RooConst.DBKEY_TOPIC_LIKES)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        return this.getIds(map.get(tid));
    }

    /**
     * 关注一个用户
     *
     * @param uid    被关注的用户
     * @param fansId 粉丝
     */
    public void follow(Long uid, Long fansId) {

        // uid的粉丝+1
        Map<Long, long[]> map = db.treeMap("follow")
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        map.put(uid, ArrayUtils.append(map.get(uid), fansId));

        // fansId的关注列表+1
        Map<Long, long[]> mapFollowing = db.treeMap("following")
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        mapFollowing.put(fansId, ArrayUtils.append(mapFollowing.get(fansId), uid));
    }

    /**
     * 取消关注一个用户
     *
     * @param uid    关注的用户uid
     * @param fansId 我的uid
     */
    public void unfollow(Long uid, Long fansId) {
        // uid的粉丝-1
        Map<Long, long[]> map = db.treeMap("follow")
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        if (map.containsKey(uid)) {
            map.put(uid, ArrayUtils.remove(map.get(uid), fansId));
        } else {
            map.put(uid, new long[]{fansId});
        }

        // fansId的关注列表-1
        Map<Long, long[]> mapFollowing = db.treeMap("following")
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        if (mapFollowing.containsKey(uid)) {
            mapFollowing.put(fansId, ArrayUtils.remove(mapFollowing.get(fansId), uid));
        } else {
            mapFollowing.put(fansId, new long[]{uid});
        }

    }

    /**
     * 获取帖子收藏数
     *
     * @param tid
     * @return
     */
    public int getTopicFavorites(String tid) {
        Map<String, Long> topicFavoriesMap = db.hashMap(RooConst.DBKEY_TOPIC_FAVORITES)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG)
                .createOrOpen();

        if (topicFavoriesMap.containsKey(tid)) {
            return topicFavoriesMap.get(tid).intValue();
        }
        return 0;
    }

    /**
     * 收藏一个帖子
     *
     * @param uid
     * @param tid
     */
    public void favoriteTopic(Long uid, String tid) {
        Map<Long, long[]> map = db.treeMap(RooConst.DBKEY_USER_FAVORITES)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        if (map.containsKey(uid)) {
            map.put(uid, ArrayUtils.append(map.get(uid), Long.parseLong(tid, 36)));
        } else {
            map.put(uid, new long[]{Long.parseLong(tid, 36)});
        }

        Map<String, Long> topicFavoriesMap = db.hashMap(RooConst.DBKEY_TOPIC_FAVORITES)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG)
                .createOrOpen();

        if (topicFavoriesMap.containsKey(tid)) {
            topicFavoriesMap.put(tid, topicFavoriesMap.get(tid) + 1);
        } else {
            topicFavoriesMap.put(tid, 1L);
        }

    }

    /**
     * 取消收藏帖子
     *
     * @param uid
     * @param tid
     */
    public void unfavoriteTopic(Long uid, String tid) {
        Map<Long, long[]> map = db.treeMap(RooConst.DBKEY_USER_FAVORITES)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        map.put(uid, ArrayUtils.remove(map.get(uid), Long.parseLong(tid, 36)));

        Map<String, Long> topicFavoriesMap = db.hashMap(RooConst.DBKEY_TOPIC_FAVORITES)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG)
                .createOrOpen();

        if (topicFavoriesMap.containsKey(tid)) {
            topicFavoriesMap.put(tid, topicFavoriesMap.get(tid) - 1);
        }

    }

    /**
     * 帖子浏览数+1
     *
     * @param tid
     */
    public Long viewTopic(String tid) {
        Map<String, Long> map = db.treeMap(RooConst.DBKEY_TOPIC_VIEWS)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG)
                .createOrOpen();
        if (map.containsKey(tid)) {
            map.put(tid, map.get(tid) + 1);
        } else {
            map.put(tid, 1L);
        }
        return map.get(tid);
    }

    /**
     * 获取帖子点赞数
     *
     * @param tid
     * @return
     */
    public int getTopicLikes(String tid) {
        Map<String, long[]> map = db.treeMap(RooConst.DBKEY_TOPIC_LIKES)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        if (map.containsKey(tid)) {
            return map.get(tid).length;
        }
        return 0;
    }

    /**
     * 给帖子点赞
     *
     * @param uid
     * @param tid
     */
    public int likeTopic(Long uid, String tid) {
        Map<String, long[]> map = db.treeMap(RooConst.DBKEY_TOPIC_LIKES)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        if (map.containsKey(tid)) {
            map.put(tid, ArrayUtils.append(map.get(tid), uid));
        } else {
            map.put(tid, new long[]{uid});
        }
        return map.get(tid).length;
    }

    /**
     * 取消帖子点赞
     *
     * @param uid
     * @param tid
     */
    public void unlikeTopic(Long uid, String tid) {
        Map<String, long[]> map = db.treeMap(RooConst.DBKEY_USER_FAVORITES)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        map.put(tid, ArrayUtils.remove(map.get(tid), uid));
    }

    /**
     * 对帖子进行增益
     *
     * @param uid
     * @param tid
     */
    public void gainTopic(Long uid, String tid) {
        Map<String, long[]> map = db.treeMap(RooConst.DBKEY_TOPIC_GAINS)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        if (map.containsKey(tid)) {
            map.put(tid, ArrayUtils.append(map.get(tid), uid));
        } else {
            map.put(tid, new long[]{uid});
        }
    }

    /**
     * 是否进行过增益
     *
     * @param uid
     * @param tid
     * @return
     */
    public boolean isGain(Long uid, String tid) {
        Map<String, long[]> map = db.treeMap(RooConst.DBKEY_TOPIC_GAINS)
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.LONG_ARRAY)
                .counterEnable()
                .createOrOpen();
        if (!map.containsKey(tid)) {
            return false;
        }
        return Stream.of(map.get(tid)).filter(o -> uid.equals(o)).count() > 0;
    }

    private List<Long> getIds(long[] follwers) {
        if (null == follwers || follwers.length == 0) {
            return null;
        }
        List<Long> followers = new ArrayList<>(follwers.length);
        for (long follwer : follwers) {
            followers.add(follwer);
        }
        return followers;
    }

}
