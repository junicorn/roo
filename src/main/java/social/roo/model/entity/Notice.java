package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

import java.util.Date;

/**
 * 通知
 *
 * @author biezhi
 * @date 2017/8/1
 */
@Table(value = "roo_notice")
@Data
public class Notice extends ActiveRecord {
    private Long    id;
    /**
     * 标题
     */
    private String  title;
    /**
     * 发送给
     */
    private String  toUser;
    /**
     * 来自
     */
    private String  fromUser;
    /**
     * 事件类型
     */
    private String  event;
    /**
     * 0:未读 1:已读
     */
    private Integer state;
    /**
     * 通知创建时间
     */
    private Date    created;
    /**
     * 阅读时间
     */
    private Date    updated;

}
