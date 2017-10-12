package social.roo.service;

import com.blade.ioc.annotation.Bean;
import social.roo.model.entity.Node;

/**
 * 节点相关
 *
 * @author biezhi
 * @date 2017/8/3
 */
@Bean
public class NodeService {

    public Node getNode(String slug){
        return new Node().where("slug", slug).find();
    }
}