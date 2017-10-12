package social.roo.controller.topic;

import com.blade.ioc.annotation.Inject;
import com.blade.kit.StringKit;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PathParam;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.http.Request;
import com.blade.mvc.ui.RestResponse;
import com.blade.security.web.csrf.CsrfToken;
import com.blade.validator.annotation.Valid;
import social.roo.auth.Access;
import social.roo.model.dto.Auth;
import social.roo.model.dto.TopicDetailDto;
import social.roo.model.entity.Topic;
import social.roo.service.RelationService;
import social.roo.service.TopicService;
import social.roo.utils.RooUtils;

/**
 * 帖子控制器
 *
 * @author biezhi
 * @date 2017/8/2
 */
@Path("topic")
public class TopicController {

    @Inject
    private TopicService topicService;

    @Inject
    private RelationService relationService;

    /**
     * 发布新主题页面
     *
     * @return
     */
    @GetRoute("new")
    @CsrfToken(newToken = true)
    public String newTopic() {

        return "topic/new";
    }

    @GetRoute("/:tid")
    public String detail(@PathParam String tid, Request request) {
        TopicDetailDto topicDetail = topicService.getTopicDetail(tid);
        request.attribute("topic", topicDetail);
        return "topic/detail";
    }

    @Access
    @PostRoute("like/:tid")
    public RestResponse<Boolean> like(@PathParam String tid) {
        Long uid = Auth.loginUser().getUid();
        topicService.likeTopic(uid, tid, true);
        return RestResponse.ok();
    }

    @Access
    @PostRoute("unlike/:tid")
    public RestResponse<Boolean> unlike(@PathParam String tid) {
        Long uid = Auth.loginUser().getUid();
        topicService.likeTopic(uid, tid, false);
        return RestResponse.ok();
    }

    @Access
    @PostRoute("favorite/:tid")
    public RestResponse<Boolean> favorite(@PathParam String tid) {
        Long uid = Auth.loginUser().getUid();
        topicService.favoriteTopic(uid, tid, true);
        return RestResponse.ok();
    }

    @Access
    @PostRoute("unfavorite/:tid")
    public RestResponse<Boolean> unfavorite(@PathParam String tid) {
        Long uid = Auth.loginUser().getUid();
        topicService.favoriteTopic(uid, tid, true);
        return RestResponse.ok();
    }

    @Access
    @PostRoute("gain/:tid")
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
    public RestResponse publish(@Valid Topic topic) {
        String username = Auth.loginUser().getUsername();
        topic.setUsername(username);
        // emoji、xss过滤
        topic.setTitle(RooUtils.cleanContent(topic.getTitle()));
        topic.setContent(RooUtils.cleanContent(topic.getContent()));
        topicService.publish(topic);
        return RestResponse.ok();
    }

    /**
     * 修改帖子
     *
     * @return
     */
    @Access
    @PostRoute("update")
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
        topic.setTitle(RooUtils.cleanContent(topic.getTitle()));
        topic.setContent(RooUtils.cleanContent(topic.getContent()));
        topicService.updateTopic(topic);
        return RestResponse.ok();
    }

}