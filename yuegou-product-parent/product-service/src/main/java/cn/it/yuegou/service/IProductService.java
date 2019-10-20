package cn.it.yuegou.service;

import cn.it.yuegou.domain.Product;
import cn.it.yuegou.domain.Specification;
import cn.it.yuegou.query.ProductQuery;
import cn.it.yuegou.util.PageList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    /**
     * 获取显示属性
     * @param productId
     * @return
     */
    List<Specification> getViewProperties(Long productId);

    /**
     * 保存显示属性
     * @param productId
     * @param specifications
     */
    void saveViewProperties(Long productId, List<Specification> specifications);

    /**
     * 获取sku属性
     * @param productId
     * @return
     */
    List<Specification> getSkuProperties(Long productId);
}
