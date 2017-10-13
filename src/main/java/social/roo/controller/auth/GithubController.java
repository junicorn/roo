package social.roo.controller.auth;

import com.blade.ioc.annotation.Inject;
import com.blade.kit.JsonKit;
import com.blade.mvc.WebContext;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Session;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.extern.slf4j.Slf4j;
import social.roo.enums.UserRole;
import social.roo.model.dto.Auth;
import social.roo.model.dto.GithubUser;
import social.roo.model.entity.PlatformUser;
import social.roo.model.entity.Profile;
import social.roo.model.entity.User;
import social.roo.service.AccountService;
import social.roo.service.PlatformService;

import java.util.Date;

import static social.roo.RooConst.LOGIN_SESSION_KEY;

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

    @Inject
    private PlatformService platformService;

    @Inject
    private AccountService accountService;

    private static final String PROTECTED_RESOURCE_URL = "https://api.github.com/user";

    @GetRoute("signin")
    public void signin(com.blade.mvc.http.Response response) {
        String authorizationUrl = githubService.getAuthorizationUrl();
        log.info("authorizationUrl: {}", authorizationUrl);
        response.redirect(authorizationUrl);
    }

    @GetRoute("callback")
    public void callback(@Param String code, Session session, com.blade.mvc.http.Response response) throws Exception {
        log.info("Code: {}", code);
        final OAuthRequest request    = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        final Response     res        = execute(code, request);
        String             body       = res.getBody();
        GithubUser         githubUser = JsonKit.formJson(body, GithubUser.class);

        PlatformUser platformUser = platformService.getPlatformUser(githubUser.getLogin());
        if (null != platformUser) {
            // 直接登录
            User user = accountService.getUserById(platformUser.getUid());

            Auth.saveToSession(user);
            Auth.saveToCookie(user.getUid());

            log.info("登录成功");
        } else {
            // 判断当前是否已经登录
            User         loginUser = session.attribute(LOGIN_SESSION_KEY);
            PlatformUser temp      = new PlatformUser();
            temp.setAppType("github");
            temp.setCreated(new Date());
            temp.setUsername(githubUser.getLogin());

            Profile profile = new Profile();

            if (null != loginUser) {
                temp.setUid(loginUser.getUid());

                profile.setUid(loginUser.getUid());
                profile.setUsername(githubUser.getLogin());
                profile.setGithub(githubUser.getLogin());
                profile.save();

                Auth.saveToSession(loginUser);
                Auth.saveToCookie(loginUser.getUid());
            } else {
                // 创建新用户
                User user = new User();
                user.setUsername(githubUser.getLogin());
                user.setPassword("");
                user.setEmail(githubUser.getEmail());
                user.setState(1);
                user.setAvatar(githubUser.getAvatar_url());
                user.setCreated(new Date());
                user.setUpdated(new Date());
                user.setRole(UserRole.MEMBER.role());
                Long uid = user.save();
                temp.setUid(uid);

                profile.setUid(uid);
                profile.setUsername(githubUser.getLogin());
                profile.setGithub(githubUser.getLogin());
                profile.save();

                Auth.saveToSession(user);
                Auth.saveToCookie(uid);
            }
            temp.save();
            log.info("登录成功");
        }
        WebContext.response().redirect("/");
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