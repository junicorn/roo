package social.roo.annotation;

import social.roo.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录后才可以访问的路由
 *
 * @author biezhi
 * @date 2017/8/6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Access {

    UserRole value() default UserRole.MEMBER;

}
