package social.roo.utils;

import com.blade.kit.Hashids;
import com.blade.kit.StringKit;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Roo工具类
 *
 * @author biezhi
 * @date 2017/8/2
 */
public class RooUtils {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz1234567890";

    private static final Hashids hashids = new Hashids("blade-roo", 12, ALPHABET);

    public static String genTid() {
        return hashids.encode(1002, System.currentTimeMillis());
    }

    public static Long decodeId(String hash) {
        return hashids.decode(hash)[1];
    }

    public static String encodeId(Long hash) {
        return hashids.encode(1003, hash);
    }

    /**
     * 计算帖子权重
     * <p>
     * 根据点赞数、收藏数、评论数、下沉数、创建时间计算
     *
     * @param likes     点赞数：权重占比1
     * @param favorites 收藏数：权重占比2
     * @param comments  评论数：权重占比2
     * @param gains     增益数：权重占比-1
     * @param created   创建时间，越早权重越低
     * @return
     */
    public static double calcWeight(int likes, int favorites, int comments, int gains, long created) {
        long score = Math.max(likes - 1, 1) + favorites * 2 + comments * 2 - gains;
        // 投票方向
        int sign = (score == 0) ? 0 : (score > 0 ? 1 : -1);
        // 帖子争议度
        double order = Math.log10(Math.max(Math.abs(score), 1));
        // 1501748867是项目创建时间
        double seconds = created - 1501748867;
        return Double.parseDouble(String.format("%.2f", order + sign * seconds / 45000));
    }

    /**
     * 获取@的用户列表
     *
     * @param str
     * @return
     */
    public static Set<String> getAtUsers(String str) {
        Set<String> users = new HashSet<>();
        if (StringKit.isNotBlank(str)) {
            Pattern pattern = Pattern.compile("\\@([a-zA-Z_0-9-]+)\\s");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                users.add(matcher.group(1));
            }
        }

        return users;
    }

    private static Pattern[] patterns = new Pattern[]{
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };


    /**
     * 清除XSS
     * Removes all the potentially malicious characters from a string
     *
     * @param value the raw string
     * @return the sanitized string
     */
    public static String cleanXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and
            // uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);
            // Avoid null characters
/**         value = value.replaceAll("", "");***/
            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression
            scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\\'](.*?)[\\\"|\\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid e-xpression(...) expressions
            scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }

}