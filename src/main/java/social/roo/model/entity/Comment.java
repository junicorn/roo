package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

import java.util.Date;

/**
 * 评论
 *
 * @author biezhi
 * @date 2017/8/1
 */
@Table(value = "roo_notice", pk = "coid")
@Data
public class Comment extends ActiveRecord {

    private Long    coid;
    private String  cid;
    private String  author;
    private String  owner;
    private String  content;
    private String  type;
    private Date    created;
    private Integer state;

}
