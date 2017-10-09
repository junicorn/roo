package social.roo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.blade.Blade;
import com.blade.Environment;
import com.blade.event.BeanProcessor;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Order;
import com.blade.jdbc.Base;
import lombok.extern.slf4j.Slf4j;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import social.roo.Roo;
import social.roo.exception.RooException;
import social.roo.model.dto.JdbcConfig;
import social.roo.model.entity.Node;
import social.roo.model.entity.Setting;
import social.roo.utils.EmailUtils;

import java.util.Map;

/**
 * 启动时进行数据库配置
 *
 * @author biezhi
 * @date 2017/7/31
 */
@Slf4j
@Order(2)
@Bean
public class DbConfig implements BeanProcessor {

    @Override
    public void processor(Blade blade) {
        Environment         environment = blade.environment();
        Map<String, Object> map         = environment.getPrefix("jdbc");
        if (map.containsKey("database")) {
            JdbcConfig jdbcConfig = JdbcConfig.builder()
                    .driver("com.mysql.jdbc.Driver")
                    .host(map.get("host").toString())
                    .port(map.get("port").toString())
                    .username(map.get("username").toString())
                    .password(map.get("password").toString())
                    .database(map.get("database").toString())
                    .build();

            if (!checkConnect(jdbcConfig)) {
                log.info("{}", jdbcConfig);
                throw new RooException("Can not connect database, please check the jdbc config :)");
            }

            loadDatasource(jdbcConfig);
            log.info("⬢ Load database config success!");

            Roo.me().refreshSettings();
            Roo.me().refreshNodes();

            if (Roo.me().existSetting("mail.smtp.host")) {
                EmailUtils.init();
            }

        }
    }

    /**
     * 检测是否可以连接到数据库
     *
     * @param jdbcConfig
     * @return
     */
    public static boolean checkConnect(JdbcConfig jdbcConfig) {
        Sql2o sql2o = new Sql2o(jdbcConfig.getUrl(), jdbcConfig.getUsername(), jdbcConfig.getPassword());
        try (Connection connection = sql2o.open()) {
            connection.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 加载数据源
     *
     * @param jdbcConfig
     */
    public static void loadDatasource(JdbcConfig jdbcConfig) {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName(jdbcConfig.getDriver());
        dataSource.setUrl(jdbcConfig.getUrl());
        dataSource.setUsername(jdbcConfig.getUsername());
        dataSource.setPassword(jdbcConfig.getPassword());

        dataSource.setInitialSize(jdbcConfig.getInitialSize());
        dataSource.setMaxActive(jdbcConfig.getMaxActive());
        dataSource.setMinIdle(jdbcConfig.getMinIdle());
        dataSource.setMaxWait(jdbcConfig.getMaxWait());
        dataSource.setMinEvictableIdleTimeMillis(jdbcConfig.getMinEvictableIdleTimeMillis());
        dataSource.setTimeBetweenEvictionRunsMillis(jdbcConfig.getTimeBetweenEvictionRunsMillis());

        Base.open(dataSource);
    }

}