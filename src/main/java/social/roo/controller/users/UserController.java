package social.roo.controller.users;

import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PathParam;

/**
 * 用户主页、收藏
 *
 * @author biezhi
 * @date 2017/10/12
 */
@Path
public class UserController {

    @GetRoute("/@:username")
    public String showProfile(@PathParam String username) {

        return "users/profile.html";
    }

}
