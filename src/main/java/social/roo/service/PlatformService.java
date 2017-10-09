package social.roo.service;

import com.blade.ioc.annotation.Bean;
import social.roo.model.entity.PlatformUser;

/**
 * 平台授权相关
 *
 * @author biezhi
 * @date 2017/10/9
 */
@Bean
public class PlatformService {

    public PlatformUser getPlatformUser(String username) {
        PlatformUser platformUser = new PlatformUser().where("username", username).find();
        return platformUser;
    }

}
