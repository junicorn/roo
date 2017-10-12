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
@Table(value = "roo_comment", pk = "coid")
@Data
public class Comment extends ActiveRecord {

    /**
     * comment表主键
     */
    private Long    coid;
    private String  tid;
    /**
     * 评论作者
     */
    private String  author;
    /**
     * 评论所属内容作者id
     */
    private String  owner;
    /**
     * 评论内容
     */
    private String  content;
    /**
     * 评论类型
     */
    private String  type;
    /**
     * 评论生成时的GMT unix时间戳
     */
    private Date    created;
    /**
     * 状态
     */
    private Integer state;

}
