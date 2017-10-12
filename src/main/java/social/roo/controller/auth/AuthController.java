package social.roo.controller.auth;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.blade.mvc.ui.RestResponse;
import com.blade.patchca.DefaultPatchca;
import com.blade.patchca.Patchca;
import com.blade.validator.annotation.Valid;
import lombok.extern.slf4j.Slf4j;
import social.roo.model.dto.Auth;
import social.roo.model.entity.Actived;
import social.roo.model.entity.User;
import social.roo.model.param.SigninParam;
import social.roo.model.param.SignupParam;
import social.roo.service.AccountService;

import java.util.Date;

/**
 * 认证控制器
 *
 * @author biezhi
 * @date 2017/7/31
 */
@Path
@Slf4j
public class AuthController {

    @Inject
    private AccountService accountService;

    private Patchca patchca = new DefaultPatchca();

    @GetRoute("patchca")
    public void patchca(Request request, Response response) {
        try {
            patchca.render(request, response);
        } catch (Exception e) {
            log.error("Create patchca fail", e);
        }
    }

    @GetRoute("signin")
    public String signin() {
        return "auth/signin";
    }

    @GetRoute("signup")
    public String signup() {
        return "auth/signup";
    }

    /**
     * 用户登录
     *
     * @return
     */
    @PostRoute("signin")
    @JSON
    public RestResponse<User> doSignin(@Valid SigninParam signinParam) {
        RestResponse<User> restResponse = accountService.login(signinParam);
        if (restResponse.isSuccess()) {
            Auth.saveToSession(restResponse.getPayload());
            Auth.saveToCookie(restResponse.getPayload().getUid());
        }
        return restResponse;
    }

    /**
     * 用户注册
     *
     * @param signupParam
     * @param request
     * @param response
     */
    @PostRoute("signup")
    public void doSignup(@Valid SignupParam signupParam, Request request, Response response) {
        RestResponse restResponse = accountService.register(signupParam);
        if (restResponse.isSuccess()) {
            request.attribute("email", signupParam.getEmail());
            response.render("auth/signup_success");
        } else {
            response.json(response);
        }
    }

    /**
     * 账户激活
     */
    @GetRoute("active")
    public String active(@Param String code, Request request) {
        Actived actived = new Actived().where("code", code).find();
        if (actived == null) {
            request.attribute("type", "invalid");
            return "auth/active";
        }
        if (actived.getState() == 1) {
            request.attribute("type", "used");
            return "auth/active";
        }
        if (actived.getExpired().compareTo(new Date()) < 0) {
            request.attribute("type", "expires");
            return "auth/active";
        }
        accountService.active(actived);
        request.attribute("type", "success");
        return "auth/active";
    }

    @GetRoute("logout")
    public void logout() {
        Auth.logout();
    }

}
