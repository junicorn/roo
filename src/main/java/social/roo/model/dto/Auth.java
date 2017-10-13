package social.roo.model.dto;

import com.blade.kit.StringKit;
import com.blade.mvc.WebContext;
import social.roo.RooConst;
import social.roo.model.entity.User;
import social.roo.utils.RooUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static social.roo.RooConst.LOGIN_SESSION_KEY;

/**
 * @author biezhi
 * @date 2017/7/31
 */
public class Auth {

    public static boolean check() {
        return true;
    }

    /**
     * 验证某个操作的频率
     *
     * @param seconds 最近的多少秒内允许操作
     * @return 返回是否在允许的范围内
     */
    public static boolean checkFrequency(long seconds) {
        Date lastUpdated = Auth.loginUser().getUpdated();
        Date current     = new Date();
        long diff        = current.getTime() - lastUpdated.getTime();
        long diffSeconds = diff / 1000;
        if (seconds <= diffSeconds) {
            return true;
        }
        return false;
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
