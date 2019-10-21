package cn.it.yuegou.service;

import cn.it.yuegou.domain.Product;
import cn.it.yuegou.domain.Specification;
import cn.it.yuegou.query.ProductQuery;
import cn.it.yuegou.util.PageList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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

    /**
     * 保存sku属性
     * @param productId
     * @param skuProperties
     * @param skus
     */
    void saveSkuProperties(Long productId, List<Specification> skuProperties, List<Map<String, String>> skus);

    /**
     * 批量上架
     * @param longs
     */
    void onSale(List<Long> longs);
    /**
     * 批量下架
     * @param longs
     */
    void offSale(List<Long> longs);
}
