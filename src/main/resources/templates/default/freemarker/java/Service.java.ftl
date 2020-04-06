${gen.setType("service")}
package ${entity.packages.service};

import com.baomidou.mybatisplus.extension.service.IService;

import ${entity.packages.entity.full};
import java.util.List;

/**
* Service：${entity.comment}
*
* @author ${developer.author}
*/
public interface ${entity.name.service} extends IService<${entity.name.entity}> {
    String CACHE_NAME = "${table.name}";
}
