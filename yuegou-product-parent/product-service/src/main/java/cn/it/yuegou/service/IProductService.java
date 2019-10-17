package cn.it.yuegou.service;

import cn.it.yuegou.domain.Product;
import cn.it.yuegou.query.ProductQuery;
import cn.it.yuegou.util.PageList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-17
 */
public interface IProductService extends IService<Product> {

    /**
     * 分页高级查询
     * @param query
     * @return
     */
    PageList<Product> queryPage(ProductQuery query);
}
