package cn.it.yuegou.repository;

import cn.it.yuegou.domain.ProductDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author yinjunchi
 */
public interface ProductDocRepository extends ElasticsearchRepository<ProductDoc,Long> {
}
