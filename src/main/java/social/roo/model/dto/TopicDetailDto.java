package social.roo.model.dto;

import com.blade.jdbc.annotation.Transient;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 帖子详情
 *
 * @author biezhi
 * @date 2017/8/2
 */
@Data
@ToString(callSuper = true)
public class TopicDetailDto extends TopicDto {

    private Boolean popular;
    private String  content;
    private Integer likes;
    private Integer views;
    private Integer favorites;
    private Integer textType;
    private Boolean gained;

    @Transient
    private List<CommentDto> commentList;

}
