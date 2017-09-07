package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import com.blade.validator.annotation.NotEmpty;
import lombok.Data;

import java.util.Date;

/**
 * 侧边栏广告
 *
 * @author biezhi
 * @date 2017/8/2
 */
@Data
@Table(value = "roo_advert")
public class Advert extends ActiveRecord {

    private Long    id;
    /**
     * 标题
     */
    @NotEmpty(message = "标题不能为空")
    private String  title;
    /**
     * 内容
     */
    @NotEmpty(message = "内容不能为空")
    private String  content;
    private Integer sort;
    private Integer state = 0;
    private Date created = new Date();

}
