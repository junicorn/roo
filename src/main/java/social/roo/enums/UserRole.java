package social.roo.enums;

import lombok.Getter;

/**
 * @author biezhi
 * @date 2017/10/12
 */
public enum UserRole {

    GUEST(0, "游客"),
    MEMBER(1, "注册会员-所有注册用户自动属于该角色"),
    VIP(2, "VIP会员-没有特殊权限，只是一个身份象征"),
    MODERATOR(3, "版主-可以管理若干个话题下的帖子"),
    SUPER_MODERATOR(4, "超级版主-可以管理所有话题下的帖子和所有会员"),
    ADMIN(5, "管理员-享有论坛的最高权限，可以管理整个论坛，设置整个论坛的参数");

    @Getter
    private Integer id;
    @Getter
    private String  description;

    UserRole(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public String role(){
        return this.toString().toLowerCase();
    }

}
