package social.roo.hooks;

import com.blade.ioc.annotation.Bean;
import com.blade.kit.StringKit;
import com.blade.mvc.hook.Signature;
import com.blade.mvc.hook.WebHook;
import com.blade.mvc.http.Request;
import lombok.extern.slf4j.Slf4j;
import social.roo.RooConst;
import social.roo.auth.Access;
import social.roo.model.entity.User;
import social.roo.utils.RooUtils;

import static social.roo.RooConst.LOGIN_SESSION_KEY;

/**
 * @author biezhi
 * @date 2017/7/31
 */
@Slf4j
@Bean
public class BaseWebHook implements WebHook {

    @Override
    public boolean before(Signature signature) {
        Request request = signature.request();
        String  uri     = request.uri();

        log.info("{}\t{}", request.method(), uri);

        User user = request.session().attribute(RooConst.LOGIN_SESSION_KEY);
        if (null == user) {
            String hash = request.cookie(RooConst.LOGIN_COOKIE_KEY, "");
            if (StringKit.isNotBlank(hash)) {
                Long uid = RooUtils.decodeId(hash);
                user = new User().find(uid);
                if (null != user) {
                    request.session().attribute(LOGIN_SESSION_KEY, user);
                }
            }
        }

        Access access = signature.getAction().getAnnotation(Access.class);
        if (null != access && null == user) {
            signature.response().redirect("/signin");
            return false;
        }
        return true;
    }

}
