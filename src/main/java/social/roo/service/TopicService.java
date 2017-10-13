package social.roo.service;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.jdbc.page.Page;
import com.blade.kit.StringKit;
import social.roo.Roo;
import social.roo.RooConst;
import social.roo.ext.InputFilter;
import social.roo.ext.TplFunctions;
import social.roo.model.dto.Auth;
import social.roo.model.dto.CommentDto;
import social.roo.model.dto.TopicDetailDto;
import social.roo.model.dto.TopicDto;
import social.roo.model.entity.*;
import social.roo.model.param.CommentParam;
import social.roo.model.param.SearchParam;
import social.roo.utils.RooUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author biezhi
 * @date 2017/8/1
 */
@Bean
public class TopicService {

    @Inject
    private RelationService relationService;

    @Inject
    private AccountService accountService;

    @Inject
    private NodeService nodeService;

    public Page<TopicDto> getTopics(SearchParam searchParam) {

        List<Object> args  = new ArrayList<>();
        String       where = " where 1=1 ";
        if (StringKit.isNotBlank(searchParam.getQ())) {
            where += "and a.title like ? ";
            args.add("%" + searchParam.getQ() + "%");
        }

        if (StringKit.isNotBlank(searchParam.getSlug())) {
            where += "and a.node_slug = ? ";
            args.add(searchParam.getSlug());
        }

        if (searchParam.isPopular()) {
            where += "and a.popular = 1 ";
        }

        String sql = "select a.tid, a.title, a.username, b.avatar," +
                "a.node_slug as nodeSlug, a.node_title as nodeTitle," +
                "a.comments, a.created, a.replyed, a.reply_id as replyId, a.reply_user as replyUser" +
                " from roo_topic a" +
                " left join roo_user b on a.username = b.username" + where +
                " order by " + searchParam.getOrderBy();

        Page<TopicDto> topics = new TopicDto().page(searchParam.getPageRow(), sql, args.toArray());
        return topics;
    }

    public TopicDetailDto getTopicDetail(String tid) {
        String sql = "select a.tid, a.title, a.content, a.username, a.text_type as textType, b.avatar," +
                "a.node_slug as nodeSlug, a.node_title as nodeTitle," +
                "a.comments, a.created, b.avatar" +
                " from roo_topic a left join roo_user b on a.username = b.username" +
                " where a.tid = ?";

        TopicDetailDto topicDetail = new TopicDetailDto().query(sql, tid);
        if (topicDetail.getComments() > 0) {
            // 加载评论
            List<CommentDto> commentDtos = TplFunctions.comments(tid);
            topicDetail.setCommentList(commentDtos);
        }
        if (topicDetail.getTextType() == 1) {
            topicDetail.setContent(new InputFilter(topicDetail.getContent()).unicodeToEmoji().mdToHtml().toString());
        } else {
            topicDetail.setContent(new InputFilter(topicDetail.getContent()).unicodeToEmoji().toString());
        }

        topicDetail.setViews(relationService.viewTopic(tid).intValue());
        topicDetail.setLikes(relationService.getTopicLikes(tid));
        topicDetail.setFavorites(relationService.getTopicFavorites(tid));

        return topicDetail;
    }

    /**
     * 点赞、取消点赞
     *
     * @param uid
     * @param tid
     * @param isLike
     */
    public void likeTopic(Long uid, String tid, boolean isLike) {
        Topic topic = new Topic().find(tid);
        if (isLike) {
            relationService.likeTopic(uid, tid);
        } else {
            relationService.unlikeTopic(uid, tid);
        }
        this.updateWeight(tid, topic.getComments(), topic.getGains(), topic.getCreated().getTime() / 1000);
    }

    /**
     * 收藏、取消收藏
     *
     * @param uid
     * @param tid
     * @param isFavorite
     */
    public void favoriteTopic(Long uid, String tid, boolean isFavorite) {
        Topic topic = new Topic().find(tid);
        if (isFavorite) {
            relationService.favoriteTopic(uid, tid);
        } else {
            relationService.unfavoriteTopic(uid, tid);
        }
        this.updateWeight(tid, topic.getComments(), topic.getGains(), topic.getCreated().getTime() / 1000);
    }

    /**
     * 增益
     *
     * @param tid
     * @param isIncrement
     */
    public void gain(Long uid, String tid, boolean isIncrement) {

        relationService.gainTopic(uid, tid);

        Topic topic = new Topic().find(tid);
        Topic temp  = new Topic();
        if (isIncrement) {
            temp.setGains(topic.getGains() + 1);
        } else {
            temp.setGains(topic.getGains() - 1);
        }
        temp.update(tid);
        this.updateWeight(tid, topic.getComments(), temp.getGains(), topic.getCreated().getTime() / 1000);
    }

    private void updateWeight(String tid, int comments, int gains, long created) {
        int    likes     = relationService.getTopicLikes(tid);
        int    favorites = relationService.getTopicFavorites(tid);
        double weight    = RooUtils.calcWeight(likes, favorites, comments, gains, created);
        Topic  temp      = new Topic();
        temp.setWeight(weight);
        temp.update(tid);
    }

    public void updateTopic(Topic topic) {
        Topic  temp    = topic.find(topic.getTid());
        long   created = temp.getCreated().getTime() / 1000;
        double weight  = RooUtils.calcWeight(0, 0, 0, 0, created);
        topic.setWeight(weight);

        topic.update();
    }

    /**
     * 发布主题
     *
     * @param topic
     */
    public void publish(Topic topic) {

        // ①. 保存主题到数据库
        Date date = new Date();
        topic.setTid(RooUtils.genTid());
        topic.setCreated(date);
        topic.setUpdated(date);
        double weight = RooUtils.calcWeight(0, 0, 0, 0, date.getTime() / 1000);
        topic.setWeight(weight);
        topic.save();

        // ②. 全站设置帖子数+1
        // settings topics count +1
        Setting setting = new Setting();
        setting.setSkey(RooConst.SETTING_KEY_TOPICS);
        Setting topicsSetting = setting.find();
        topicsSetting.setSvalue(String.valueOf(Integer.parseInt(topicsSetting.getSvalue()) + 1));
        topicsSetting.update(RooConst.SETTING_KEY_TOPICS);

        // refresh settings
        Roo.me().refreshSettings();

        // ③. 用户发帖数+1
        Profile profile = accountService.getProfile(topic.getUsername());
        int     topics  = profile.getTopics() + 1;
        Profile temp    = new Profile();
        temp.setTopics(topics);
        temp.where("username", topic.getUsername()).update();

        // ④. 更新用户最后操作时间
        User u = new User();
        u.setUpdated(date);
        Auth.loginUser().setUpdated(date);
        u.where("username", topic.getUsername()).update();

        // ⑤. 节点下帖子数+1
        Node node       = nodeService.getNode(topic.getNodeSlug());
        int  nodeTopics = node.getTopics() + 1;
        Node nodeTemp   = new Node();
        nodeTemp.setTopics(nodeTopics);
        nodeTemp.where("slug", topic.getNodeSlug()).update();

        // ⑥. 通知@的人
        Set<String> atUsers = RooUtils.getAtUsers(topic.getContent());
        if (atUsers.size() > 0) {
            atUsers.forEach(username -> {
                Notice notice = new Notice();
                notice.setToUser(username);
                notice.setFromUser(topic.getUsername());
                notice.setTitle(topic.getTitle());
                notice.setEvent("topic_at");
                notice.setState(0);
                notice.setCreated(new Date());
                notice.save();
            });
        }
    }

    /**
     * 主题评论
     *
     * @param commentParam
     */
    public void comment(CommentParam commentParam) {

        // ①. 更新主题权重
        Date date = new Date();

        Topic topic = new Topic().find(commentParam.getTid());

        int likes     = relationService.getTopicLikes(topic.getTid());
        int favorites = relationService.getTopicFavorites(topic.getTid());

        Topic temp = new Topic();
        temp.setComments(topic.getComments() + 1);
        temp.setUpdated(date);
        temp.setReplyed(date);
        temp.setReplyUser(commentParam.getAuthor());
        double weight = RooUtils.calcWeight(likes, favorites, temp.getComments(), topic.getGains(), date.getTime() / 1000);
        temp.setWeight(weight);
        temp.update(topic.getTid());

        // ②. 保存评论
        Comment comment = new Comment();
        comment.setTid(commentParam.getTid());
        comment.setAuthor(commentParam.getAuthor());
        comment.setOwner(commentParam.getOwner());
        comment.setContent(new InputFilter(commentParam.getContent()).cleanXss().emojiToUnicode().toString());
        comment.setType(commentParam.getType());
        comment.setCreated(date);
        comment.setState(1);
        comment.save();

        // ③. 更新用户评论数
        Profile profile   = accountService.getProfile(commentParam.getAuthor());
        Profile upProfile = new Profile();
        upProfile.setComments(profile.getComments() + 1);
        upProfile.update(profile.getUid());

        // ④. 更新最后操作时间
        User u = new User();
        u.setUpdated(date);
        Auth.loginUser().setUpdated(date);
        u.where("username", topic.getUsername()).update();

        // ⑤. 更新全局统计
        Setting setting = new Setting();
        setting.setSkey(RooConst.SETTING_KEY_COMMENTS);
        Setting commentsSetting = setting.find();
        commentsSetting.setSvalue(String.valueOf(Integer.parseInt(commentsSetting.getSvalue()) + 1));
        commentsSetting.update(RooConst.SETTING_KEY_COMMENTS);

        // refresh settings
        Roo.me().refreshSettings();

        // ⑤. 处理@用户

    }
}