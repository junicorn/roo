package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

/**
 * 个人详细信息
 *
 * @author biezhi
 * @date 2017/8/2
 */
@Data
@Table(value = "roo_profile", pk = "uid")
public class Profile extends ActiveRecord {

    /**
     * 用户id
     */
    private Long    uid;
    /**
     * 用户名
     */
    private String  username;
    /**
     * 发布的帖子数
     */
    private Integer topics;
    /**
     * 评论的帖子数
     */
    private Integer comments;
    /**
     * 收藏数
     */
    private Integer favorites;
    /**
     * 粉丝数
     */
    private Integer followers;
    /**
     * 所在位置
     */
    private String  location;
    /**
     * 个人主页
     */
    private String  website;
    /**
     * github账号
     */
    private String  github;
    /**
     * 微博账号
     */
    private String  weibo;
    /**
     * 个性签名
     */
    private String  signature;

}
