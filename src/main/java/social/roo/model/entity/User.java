package social.roo.model.entity;

import com.blade.jdbc.annotation.Table;
import com.blade.jdbc.core.ActiveRecord;
import lombok.Data;

import java.util.Date;

/**
 * 用户
 *
 * @author biezhi
 * @date 2017/8/1
 */
@Table(value = "roo_user", pk = "uid")
@Data
public class User extends ActiveRecord {

    /**
     * 用户id
     */
    private Long    uid;
    /**
     *用户名
     */
    private String  username;
    /**
     * 用户密码
     */
    private String  password;
    /**
     * 用户邮箱
     */
    private String  email;
    /**
     *
     */
    private String  avatar;
    /**
     * 角色
     */
    private String  role;
    /**
     * 注册时间
     */
    private Date    created;
    /**
     * 最后一次操作时间
     */
    private Date    updated;
    /**
     * 最后一次登录时间
     */
    private Date    logined;
    /**
     * 用户状态 0:未激活 1:正常 2:停用 3:注销
     */
    private Integer state;

}