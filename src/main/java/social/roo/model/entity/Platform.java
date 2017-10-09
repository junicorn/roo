package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

import java.util.Date;

/**
 * 第三方授权平台
 *
 * @author biezhi
 * @date 2017/10/9
 */
@Table(value = "roo_platform")
@Data
public class Platform extends ActiveRecord {
    private Integer id;
    private String  appName;
    private String  appKey;
    private String  appSecret;
    private Date    created;
}
