package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

import java.util.Date;

/**
 * 激活码
 *
 * @author biezhi
 * @date 2017/8/2
 */
@Data
@Table(value = "roo_actived")
public class Actived extends ActiveRecord {

    private Long    id;
    private Long    uid;
    private String  username;
    private String  email;
    private String  code;
    /**
     * 激活状态
     * 0未激活 1已激活
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date    created;
    /**
     * 过期时间
     */
    private Date    expired;

}
