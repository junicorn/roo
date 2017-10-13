package social.roo;

import com.blade.Blade;
import com.blade.security.web.csrf.CsrfMiddleware;
import com.blade.validator.ValidatorMiddleware;

/**
 * Roo启动类
 *
 * @author biezhi
 * @date 2017/7/31
 */
public class Application {

    public static void main(String[] args) {
//        System.setProperty("java.net.useSystemProxies", "true");
        Blade.me().use(new ValidatorMiddleware(), new CsrfMiddleware())
                .start(Application.class, args);
    }

}
