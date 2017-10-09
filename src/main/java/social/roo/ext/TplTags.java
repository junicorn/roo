package social.roo.ext;

import jetbrick.collection.TimedSizeCache;
import jetbrick.template.runtime.JetTagContext;

import java.io.IOException;

/**
 * 模板自定义标签
 *
 * @author biezhi
 * @date 2017/10/9
 */
public class TplTags {

    private static final TimedSizeCache cache = new TimedSizeCache(128);

    /**
     * 标签缓存
     *
     * #tag cache("CACHE-TIPS-QUOTES", 86400)
     *  今天天气: ${randTips(1)}
     * #end
     *
     * @param ctx
     * @param key
     * @param timeout
     * @throws IOException
     */
    public static void cache(JetTagContext ctx, String key, int timeout) throws IOException {
        Object value = cache.get(key);
        if (value == null) {
            value = ctx.getBodyContent();
            cache.put(key, value, timeout);
        }
        ctx.getWriter().print(value.toString());
    }

}
