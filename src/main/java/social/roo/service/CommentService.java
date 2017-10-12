package social.roo.service;

import com.blade.ioc.annotation.Bean;
import social.roo.model.dto.CommentDto;
import social.roo.utils.RooUtils;

import java.util.List;

/**
 * @author biezhi
 * @date 2017/8/2
 */
@Bean
public class CommentService {

    public List<CommentDto> getComments(String tid) {
        String sql = "select a.coid, a.author, b.avatar, a.content, a.created" +
                " from roo_comment a" +
                " left join roo_user b on a.author = b.username" +
                " where a.tid = ? order by a.coid asc";

        List<CommentDto> list = new CommentDto().queryAll(sql, tid);
        list.forEach(dto -> dto.setContent(RooUtils.mdToHtml(dto.getContent())));
        return list;
    }

}
