package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import com.blade.validator.annotation.Length;
import com.blade.validator.annotation.NotEmpty;
import lombok.Data;

import java.util.Date;

/**
 * 主题
 *
 * @author biezhi
 * @date 2017/8/1
 */
@Table(value = "roo_topic", pk = "tid")
@Data
public class Topic extends ActiveRecord {

    private String  tid;
    /**
     * 所属节点
     */
    @NotEmpty(message = "请选择节点")
    private String  nodeSlug;
    /**
     * 节点名称
     */
    private String  nodeTitle;
    /**
     * 主题题目
     */
    @NotEmpty(message = "请请输入主题标题")
    private String  title;
    /**
     * 主题内容
     */
    @NotEmpty(message = "请请输入主题内容")
    private String  content;
    /**
     * 创建人
     */
    private String  username;
    /**
     * 评论数
     */
    private Integer comments;
    /**
     *
     */
    private Integer gains;
    /**
     * 帖子权重
     */
    private Double  weight;

    /**
     * 1: markdown 2: html
     */
    private Integer textType;

    /**
     * 是否是精华贴
     */
    private Boolean popular;
    /**
     * 最后回复人
     */
    private String  replyUser;
    /**
     * 创建时间
     */
    private Date    created;
    /**
     * 更新时间
     */
    private Date    updated;
    /**
     * 最后回复时间
     */
    private Date    replyed;

}