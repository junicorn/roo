package social.roo.controller.auth;

import com.blade.ioc.annotation.Inject;
import com.blade.kit.JsonKit;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.extern.slf4j.Slf4j;
import social.roo.model.dto.GithubUser;

/**
 * Github认证控制器
 *
 * @author biezhi
 * @date 2017/10/9
 */
@Slf4j
@Path("auth/github")
public class GithubController {

    @Inject
    private OAuth20Service githubService;

    private static final String PROTECTED_RESOURCE_URL = "https://api.github.com/user";

    @GetRoute("callback")
    public void callback(@Param String code) throws Exception {
        log.info("Code: {}", code);
        final OAuthRequest request  = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        final Response     response = execute(code, request);
        String             body     = response.getBody();
        log.info("body: {}", body);
        GithubUser githubUser = JsonKit.formJson(body, GithubUser.class);
        System.out.println(githubUser);

    }

    private Response execute(String code, OAuthRequest oAuthRequest) {
        try {
            final OAuth2AccessToken accessToken = githubService.getAccessToken(code);
            githubService.signRequest(accessToken, oAuthRequest);
            final Response response = githubService.execute(oAuthRequest);
            log.info("code: {}", response.getCode());
            log.info("response: {}", response.getBody());
            return response;
        } catch (Exception e) {
            log.error("execute error", e);
        }
        return null;
    }

}