package social.roo.auth;

import com.blade.mvc.hook.Signature;
import com.blade.mvc.hook.WebHook;
import com.blade.mvc.http.Response;
import com.blade.mvc.http.Session;
import social.roo.RooConst;
import social.roo.model.entity.User;

import java.lang.reflect.Method;

/**
 * 登录权限验证中间件
 *
 * @author biezhi
 * @date 2017/8/6
 */
public class AuthMiddleware implements WebHook {

    @Override
    public boolean before(Signature signature) {
        Method method = signature.getAction();
        Access access = method.getAnnotation(Access.class);
        if (null == access) {
            return true;
        }
        Response response = signature.response();
        Session  session  = signature.getRequest().session();
        User     user     = session.attribute(RooConst.LOGIN_SESSION_KEY);
        if (null == user) {
            response.badRequest().text("Bad Request.");
            return false;
        }
        if ("member".equals(access.value())) {
            return true;
        }
        if (!access.value().equals(user.getRole())) {
            response.badRequest().text("There is no access to");
            return false;
        }
        return true;
    }

}
