package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

/**
 * 侧边栏提示
 *
 * @author biezhi
 * @date 2017/10/9
 */
@Data
@Table(value = "roo_tips")
public class Tips extends ActiveRecord {
    private Integer id;
    private String  content;
    private String  foot;
    private Integer type;
}
