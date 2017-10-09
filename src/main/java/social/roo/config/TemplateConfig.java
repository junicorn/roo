package social.roo.config;

import com.blade.Blade;
import com.blade.event.BeanProcessor;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Order;
import com.blade.mvc.view.template.JetbrickTemplateEngine;
import social.roo.Roo;
import social.roo.ext.TplFunctions;
import social.roo.ext.TplTags;
import social.roo.model.dto.Auth;
import jetbrick.template.resolver.GlobalResolver;

/**
 * 模板引擎、数据库配置加载
 *
 * @author biezhi
 * @date 2017/7/31
 */
@Order(1)
@Bean
public class TemplateConfig implements BeanProcessor {

    @Override
    public void processor(Blade blade) {
        JetbrickTemplateEngine templateEngine = new JetbrickTemplateEngine();
        GlobalResolver         resolver       = templateEngine.getGlobalResolver();
        resolver.registerFunctions(TplFunctions.class);
        resolver.registerMethods(Auth.class);
        resolver.registerTags(TplTags.class);

        Roo.me().globalContext(templateEngine.getGlobalContext());
        Roo.me().context(String.class, "version", blade.environment().get("app.version", "0.0.1"));

        blade.templateEngine(templateEngine);
    }

}
