package social.roo.controller.topic;

import com.blade.ioc.annotation.Inject;
import com.blade.kit.StringKit;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import com.blade.security.web.csrf.CsrfToken;
import com.blade.validator.annotation.Valid;
import lombok.extern.slf4j.Slf4j;
import social.roo.RooConst;
import social.roo.annotation.Access;
import social.roo.enums.ErrorCode;
import social.roo.ext.InputFilter;
import social.roo.model.dto.Auth;
import social.roo.model.dto.TopicDetailDto;
import social.roo.model.entity.Topic;
import social.roo.model.param.CommentParam;
import social.roo.service.NodeService;
import social.roo.service.RelationService;
import social.roo.service.TopicService;

/**
 * 帖子控制器
 *
 * @author biezhi
 * @date 2017/8/2
 */
@Path("topic")
@Slf4j
public class TopicController {

    @Inject
    private TopicService topicService;

    @Inject
    private RelationService relationService;

    @Inject
    private NodeService nodeService;

    /**
     * 发布新主题页面
     *
     * @return
     */
    @Access
    @GetRoute("new")
    @CsrfToken(newToken = true)
    public String newTopic() {
        return "topic/new";
    }

    /**
     * 主题详情页面
     *
     * @param tid
     * @param request
     * @return
     */
    @CsrfToken(newToken = true)
    @GetRoute("/:tid")
    public String detail(@PathParam String tid, Request request) {
        // 内部会增加浏览量
        TopicDetailDto topicDetail = topicService.getTopicDetail(tid);
        request.attribute("topic", topicDetail);
        return "topic/detail";
    }

    /**
     * 喜欢一个帖子
     *
     * @param tid
     * @return
     */
    @Access
    @PostRoute("like/:tid")
    @JSON
    public RestResponse<Boolean> like(@PathParam String tid) {
        Long uid = Auth.loginUser().getUid();
        topicService.likeTopic(uid, tid, true);
        return RestResponse.ok();
    }

    /**
     * 取消喜欢一个帖子
     *
     * @param tid
     * @return
     */
    @Access
    @PostRoute("unlike/:tid")
    @JSON
    public RestResponse<Boolean> unlike(@PathParam String tid) {
        Long uid = Auth.loginUser().getUid();
        topicService.likeTopic(uid, tid, false);
        return RestResponse.ok();
    }

    /**
     * 收藏一个帖子
     *
     * @param tid
     * @return
     */
    @Access
    @PostRoute("favorite/:tid")
    @JSON
    public RestResponse<Boolean> favorite(@PathParam String tid) {
        Long uid = Auth.loginUser().getUid();
        topicService.favoriteTopic(uid, tid, true);
        return RestResponse.ok();
    }

    /**
     * 取消收藏帖子
     *
     * @param tid
     * @return
     */
    @Access
    @PostRoute("unfavorite/:tid")
    @JSON
    public RestResponse<Boolean> unfavorite(@PathParam String tid) {
        Long uid = Auth.loginUser().getUid();
        topicService.favoriteTopic(uid, tid, true);
        return RestResponse.ok();
    }

    /**
     * 帖子增益，用户只可以对帖子进行一次增益操作
     *
     * @param tid
     * @param num
     * @return
     */
    @Access
    @PostRoute("gain/:tid")
    @JSON
    public RestResponse<Boolean> gain(@PathParam String tid, int num) {
        Long uid = Auth.loginUser().getUid();
        if (relationService.isGain(uid, tid)) {
            return RestResponse.fail("请勿重复操作");
        }
        topicService.gain(uid, tid, num > 0);
        return RestResponse.ok();
    }

    /**
     * 发布帖子
     *
     * @return
     */
    @Access
    @PostRoute("publish")
    @CsrfToken(valid = true)
    @JSON
    public RestResponse publish(@Valid Topic topic) {
        // 发帖频率太快
        if (!Auth.checkFrequency(RooConst.FREQUENCY_PUBLISH_TOPIC)) {
            return RestResponse.fail(ErrorCode.OPT_TOO_FAST.getCode(), "发帖频率太快了，每 " + RooConst.FREQUENCY_PUBLISH_TOPIC + " 秒可发布一次主题");
        }

        String username = Auth.loginUser().getUsername();
        topic.setUsername(username);
        // emoji、xss过滤
        topic.setTitle(new InputFilter(topic.getTitle()).cleanXss().toString());
        topic.setContent(new InputFilter(topic.getContent()).cleanXss().emojiToUnicode().toString());
        try {
            topicService.publish(topic);
            return RestResponse.ok();
        } catch (Exception e) {
            log.error("主题发布失败", e);
            return RestResponse.fail("主题发布失败");
        }
    }

    /**
     * 修改帖子
     *
     * @return
     */
    @Access
    @PostRoute("update")
    @CsrfToken(valid = true)
    @JSON
    public RestResponse update(@Valid Topic topic) {
        if (StringKit.isBlank(topic.getTid())) {
            return RestResponse.fail("非法请求");
        }
        String username = Auth.loginUser().getUsername();
        Topic  temp     = new Topic().find(topic.getTid());
        if (null == temp || !temp.getUsername().equals(username)) {
            return RestResponse.fail("非法请求");
        }
        // emoji、xss过滤
        topic.setTitle(new InputFilter(topic.getTitle()).cleanXss().toString());
        topic.setContent(new InputFilter(topic.getContent()).cleanXss().emojiToUnicode().toString());
        topicService.updateTopic(topic);
        return RestResponse.ok();
    }

    /**
     * 评论帖子
     *
     * @param commentParam
     * @return
     */
    @Access
    @PostRoute("comment")
    @CsrfToken(valid = true)
    @JSON
    public RestResponse comment(@Param CommentParam commentParam) {
        // 回复频率太快
        if (!Auth.checkFrequency(RooConst.FREQUENCY_COMMENT)) {
            return RestResponse.fail(ErrorCode.OPT_TOO_FAST.getCode(), "回复频率太快了，每 " + RooConst.FREQUENCY_COMMENT + " 秒可回复一次");
        }
        try {
            commentParam.setAuthor(Auth.loginUser().getUsername());
            topicService.comment(commentParam);
            return RestResponse.ok();
        } catch (Exception e) {
            log.error("评论发布失败", e);
            return RestResponse.fail("评论发布失败");
        }
    }

}