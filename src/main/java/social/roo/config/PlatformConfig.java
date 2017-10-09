package social.roo.config;

import com.blade.Blade;
import com.blade.event.BeanProcessor;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Order;
import com.blade.kit.StringKit;
import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Random;

/**
 * @author biezhi
 * @date 2017/10/9
 */
@Slf4j
@Order(3)
@Bean
public class PlatformConfig implements BeanProcessor {

    @Override
    public void preHandle(Blade blade) {
        Map<String, Object> githubConfig = blade.environment().getPrefix("platform.github");

        String clientId     = githubConfig.get("client_id").toString();
        String clientSecret = githubConfig.get("client_secret").toString();
        String callbackUrl  = githubConfig.get("callback_url").toString();
//        String scope        = githubConfig.getOrDefault("scope", "").toString();

        String secretState = "secret" + new Random().nextInt(999_999);

        if (StringKit.isBlank(clientId) || StringKit.isBlank(clientSecret)) {
            log.warn("未配置Github授权信息");
            return;
        }

        OAuth20Service githubService = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .state(secretState)
                .callback(callbackUrl)
                .debug()
                .build(GitHubApi.instance());

        if (null != githubService) {
            blade.register(githubService);
        }

    }

    @Override
    public void processor(Blade blade) {
    }

}
