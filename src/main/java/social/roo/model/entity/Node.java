package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

/**
 * 节点
 *
 * @author biezhi
 * @date 2017/8/1
 */
@Table(value = "roo_node")
@Data
public class Node extends ActiveRecord {

    /**
     * id
     */
    private Integer id;
    /**
     * 父id
     */
    private Integer pid;
    /**
     *
     */
    private String  slug;
    /**
     * 节点名称
     */
    private String  title;
    /**
     * 节点描述
     */
    private String  description;
    /**
     * 节点下的主题数
     */
    private Integer topics;
    private Integer state;
}
