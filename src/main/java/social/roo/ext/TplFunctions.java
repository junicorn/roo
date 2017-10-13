package social.roo.ext;

import com.blade.kit.StringKit;
import social.roo.Roo;
import social.roo.RooConst;
import social.roo.model.dto.CommentDto;
import social.roo.model.entity.Tips;
import social.roo.utils.RooUtils;

import java.util.List;

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

    public static List<CommentDto> comments(String tid){
        String sql = "select a.coid, a.author, b.avatar, a.content, a.created" +
                " from roo_comment a" +
                " left join roo_user b on a.author = b.username" +
                " where a.tid = ? order by a.coid asc";

        List<CommentDto> list = new CommentDto().queryAll(sql, tid);
        list.forEach(dto -> dto.setContent(new InputFilter(dto.getContent()).mdToHtml().toString()));
        return list;
    }

}