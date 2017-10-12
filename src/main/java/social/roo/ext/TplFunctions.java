package social.roo.ext;

import com.blade.kit.StringKit;
import social.roo.Roo;
import social.roo.RooConst;
import social.roo.model.entity.Tips;

/**
 * 模板函数
 *
 * @author biezhi
 * @date 2017/7/31
 */
public class TplFunctions {

    /**
     * 是否启用某个插件
     *
     * @param key
     * @return
     */
    public static boolean enabled(String key) {
        String value = Roo.me().getOrDefault("plugin." + key, "false");
        return "true".equals(value);
    }

    public static String siteUrl() {
        return siteUrl("");
    }

    public static String siteUrl(String sub) {
        if (StringKit.isBlank(sub)) {
            return Roo.me().getSetting("site_url");
        }
        String url = Roo.me().getSetting("site_url") + sub;
        return url;
    }

    /**
     * 根据类别随机获取一个提示
     *
     * @param type 提示类型
     * @return
     * @see RooConst#TIP_QUOTES
     * @see RooConst#TIP_COMMUNITY
     */
    public static String randTips(int type) {
        String sql  = "select * from roo_tips where `type` = ? order by rand() limit 1";
        Tips   tips = new Tips().query(sql, type);
        String text = "<p>" + tips.getContent() + "</p>";
        if (StringKit.isNotBlank(tips.getFoot())) {
            text += "<p class='tips-foot'>——" + tips.getFoot() + "</p>";
        }
        return text;
    }

}