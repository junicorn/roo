package social.roo;

/**
 * Roo常量
 *
 * @author biezhi
 * @date 2017/7/31
 */
public interface RooConst {

    String RELATE_DBNAME         = "db/roo_relate.db";
    String DBKEY_FOLLOW          = "follow";
    String DBKEY_FOLLOWING       = "following";
    String DBKEY_USER_FAVORITES  = "user:favorites";
    String DBKEY_TOPIC_LIKES     = "topic:likes";
    String DBKEY_TOPIC_VIEWS     = "topic:views";
    String DBKEY_TOPIC_FAVORITES = "topic:favorites";
    String DBKEY_TOPIC_GAINS     = "topic:gains";

    String SETTING_KEY_USERS    = "site_users";
    String SETTING_KEY_TOPICS   = "site_topics";
    String SETTING_KEY_COMMENTS = "site_comments";

    String LOGIN_SESSION_KEY = "login_user";
    String LOGIN_COOKIE_KEY  = "ROO_U_ID";

    // 名人名言类别
    int TIP_QUOTES    = 1;
    // 社区提示
    int TIP_COMMUNITY = 2;

    // 发帖频率
    long FREQUENCY_PUBLISH_TOPIC = 60;
    // 回复频率
    long FREQUENCY_COMMENT       = 30;
}