package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

import java.util.Date;

/**
 * 系统日志
 *
 * @author biezhi
 * @date 2017/8/2
 */
@Data
@Table(value = "roo_logs")
public class SysLog extends ActiveRecord {


    private Long   id;
    private String title;
    /**
     * 日志内容
     */
    private String content;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 日志产生时间
     */
    private Date   created;

}
