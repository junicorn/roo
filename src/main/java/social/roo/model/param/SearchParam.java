package social.roo.model.param;

import lombok.*;

/**
 * @author biezhi
 * @date 2017/8/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SearchParam extends PageParam {

    private String q;
    private String slug;
    private boolean popular;
    private String orderBy = "a.weight desc, a.created desc";

}
