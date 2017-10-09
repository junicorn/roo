package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

import java.util.Date;

/**
 * 第三方授权用户
 *
 * @author biezhi
 * @date 2017/10/9
 */
@Table(value = "roo_platform_user")
@Data
public class PlatformUser extends ActiveRecord {
    private Integer id;
    private String  appType;
    private String  username;
    private Long    uid;
    private Date    created;
}
