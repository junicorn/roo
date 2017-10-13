package social.roo.ext;

import com.blade.security.web.filter.HTMLFilter;
import com.vdurmont.emoji.EmojiParser;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

/**
 * @author biezhi
 * @date 2017/10/13
 */
public class InputFilter {

    private String value;

    public InputFilter(String value) {
        this.value = value;
    }

    public InputFilter mdToHtml() {
        List<Extension> extensions = Arrays.asList(TablesExtension.create());
        Parser          parser     = Parser.builder().extensions(extensions).build();
        Node            document   = parser.parse(this.value);
        HtmlRenderer    renderer   = HtmlRenderer.builder().extensions(extensions).build();
        this.value = renderer.render(document);
        return this;
    }

    public InputFilter htmlToMd() {

        return this;
    }

    public InputFilter htmlToText() {
        this.value = this.value.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        return this;
    }

    public InputFilter emojiToUnicode() {
        this.value = EmojiParser.parseToUnicode(this.value);
        return this;
    }

    public InputFilter unicodeToEmoji() {
        this.value = EmojiParser.parseToHtmlDecimal(this.value);
        return this;
    }

    public InputFilter cleanXss() {
        this.value = new HTMLFilter().filter(this.value);
        return this;
    }

    public InputFilter showHref(boolean blankTab) {

        return this;
    }

    public String toString() {
        return this.value;
    }

}
