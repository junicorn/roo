package social.roo.model.dto;

import com.blade.kit.StringKit;
import com.blade.mvc.WebContext;
import social.roo.RooConst;
import social.roo.model.entity.User;
import social.roo.utils.RooUtils;

import static social.roo.RooConst.LOGIN_SESSION_KEY;

/**
 * @author biezhi
 * @date 2017/7/31
 */
public class Auth {

    public static boolean check() {
        return true;
    }

    public static boolean hasRole(String... roles) {
        return true;
    }

    public static User loginUser() {
        User user = WebContext.request().session().attribute(RooConst.LOGIN_SESSION_KEY);
        return user;
    }

    public static void saveToSession(User user) {
        WebContext.request().session().attribute(RooConst.LOGIN_SESSION_KEY, user);
    }

    public static User getUserByCookie() {
        String hash = WebContext.request().cookie(RooConst.LOGIN_COOKIE_KEY, "");
        if (StringKit.isNotBlank(hash)) {
            Long uid = RooUtils.decodeId(hash);
            return new User().find(uid);
        }
        return null;
    }

    public static void saveToCookie(Long uid) {
        String hash = RooUtils.encodeId(uid);
        WebContext.response().cookie(RooConst.LOGIN_COOKIE_KEY, hash, 3600 * 7);
    }

    public static void logout() {
        WebContext.request().session().removeAttribute(LOGIN_SESSION_KEY);
        WebContext.response().removeCookie(RooConst.LOGIN_COOKIE_KEY);
        WebContext.response().redirect("/");
    }

}
