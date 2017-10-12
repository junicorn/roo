package social.roo.model.param;

import lombok.Data;

/**
 * @author biezhi
 * @date 2017/10/12
 */
@Data
public class CommentParam {

    private String tid;
    private String content;
    private String author;
    private String owner;
    private String type;

}
