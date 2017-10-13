package social.roo.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.jdbc.page.Page;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PathParam;
import com.blade.mvc.http.Request;
import social.roo.model.dto.TopicDto;
import social.roo.model.entity.Node;
import social.roo.model.param.SearchParam;
import social.roo.service.TopicService;

/**
 * 首页控制器
 *
 * @author biezhi
 * @date 2017/8/1
 */
@Path
public class IndexController {

    @Inject
    private TopicService topicService;

    @GetRoute
    public String home(SearchParam searchParam, Request request) {
        searchParam = null == searchParam ? new SearchParam() : searchParam;
        Page<TopicDto> topicDtoPage = topicService.getTopics(searchParam);
        request.attribute("topics", topicDtoPage.getRows());
        return "home";
    }

    @GetRoute("news")
    public String news() {
        return "news";
    }

    @GetRoute("wiki")
    public String wiki() {
        return "wiki";
    }

    @GetRoute("about")
    public String about() {
        return "about";
    }

    @GetRoute("popular")
    public String popular(Request request) {
        return this.popular(1, request);
    }

    @GetRoute("popular/:page")
    public String popular(@PathParam Integer page, Request request) {
        SearchParam searchParam = SearchParam.builder().popular(true).build();
        searchParam.setOrderBy("a.created desc");
        searchParam.setPage(page);

        Page<TopicDto> topicDtoPage = topicService.getTopics(searchParam);
        request.attribute("topics", topicDtoPage.getRows());
        return "popular";
    }

    @GetRoute("faq")
    public String faq() {
        return "faq";
    }

    @GetRoute("sites")
    public String sites() {
        return "sites";
    }

    @GetRoute("contact")
    public String contact() {
        return "contact";
    }

    @GetRoute("topics")
    public String topics(SearchParam searchParam, Request request) {
        return this.topicsPage(1, searchParam, request);
    }

    @GetRoute("topics/:page")
    public String topicsPage(@PathParam Integer page, SearchParam searchParam, Request request) {
        searchParam = null == searchParam ? new SearchParam() : searchParam;
        searchParam.setPage(page);
        searchParam.setOrderBy("a.created desc");
        Page<TopicDto> topicDtoPage = topicService.getTopics(searchParam);
        request.attribute("topicPage", topicDtoPage);
        return "topics";
    }

    @GetRoute("go/:slug")
    public String goNode(@PathParam String slug, SearchParam searchParam, Request request) {
        searchParam = null == searchParam ? new SearchParam() : searchParam;
        Node           node         = new Node().where("slug", slug).find();
        Page<TopicDto> topicDtoPage = topicService.getTopics(searchParam);
        request.attribute("node", node);
        request.attribute("topicPage", topicDtoPage);
        return "topic/node";
    }

}
