package cn.it.yuegou.service.impl;

import cn.it.yuegou.domain.Product;
import cn.it.yuegou.domain.ProductExt;
import cn.it.yuegou.domain.Sku;
import cn.it.yuegou.domain.Specification;
import cn.it.yuegou.mapper.ProductExtMapper;
import cn.it.yuegou.mapper.ProductMapper;
import cn.it.yuegou.mapper.SkuMapper;
import cn.it.yuegou.mapper.SpecificationMapper;
import cn.it.yuegou.query.ProductQuery;
import cn.it.yuegou.service.IProductService;
import cn.it.yuegou.util.PageList;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-17
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductExtMapper productExtMapper;
    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Override
    @Transactional
    public boolean save(Product product) {
        product.setCreateTime(System.currentTimeMillis());
        boolean save = super.save(product);
        ProductExt productExt = product.getExt();
        productExt.setProductId(product.getId());
        productExtMapper.insert(productExt);
        return save;
    }

    @Override
    public boolean updateById(Product product) {
        product.setUpdateTime(System.currentTimeMillis());
        boolean updateById = super.updateById(product);
        productExtMapper.updateById(product.getExt());
        return updateById;
    }

    @Override
    public PageList<Product> queryPage(ProductQuery query) {
        IPage<Product> brandIPage = baseMapper.queryPage(new Page(query.getPageNum(), query.getPageSize()), query);
        PageList<Product> list = new PageList<>(brandIPage.getTotal(), brandIPage.getRecords());
        return list;
    }

    @Override
    public List<Specification> getViewProperties(Long productId) {
        List<Specification> specifications = null;
        Product product = baseMapper.selectById(productId);
        String viewProperties = product.getViewProperties();
        if (StringUtils.isEmpty(viewProperties)){
            specifications = specificationMapper.selectList(new QueryWrapper<Specification>()
                    .eq("product_type_id", product.getProductTypeId()).eq("isSku", 0));
        }else {
            specifications =JSONArray.parseArray(viewProperties, Specification.class);
        }
        return specifications;
    }

    @Override
    public void saveViewProperties(Long productId, List<Specification> viewProperties) {
        Product product = baseMapper.selectById(productId);
        product.setViewProperties(JSONArray.toJSONString(viewProperties));
        baseMapper.updateById(product);
    }

    @Override
    public List<Specification> getSkuProperties(Long productId) {
        List<Specification> specifications = null;
        Product product = baseMapper.selectById(productId);
        String skuProperties = product.getSkuProperties();
        if (StringUtils.isEmpty(skuProperties)){
            specifications = specificationMapper.selectList(new QueryWrapper<Specification>()
                    .eq("product_type_id", product.getProductTypeId()).eq("isSku", 1));
        }else {
            specifications =JSONArray.parseArray(skuProperties, Specification.class);
        }
        return specifications;
    }

    /**
     * 保存sku属性
     * @param productId
     * @param skuProperties
     * @param skus
     */
    @Override
    public void saveSkuProperties(Long productId, List<Specification> skuProperties, List<Map<String, String>> skus) {
        Product product = baseMapper.selectById(productId);
        product.setSkuProperties(JSONArray.toJSONString(skuProperties));
        baseMapper.updateById(product);
        skuMapper.delete(new QueryWrapper<Sku>().eq("product_id",productId));
        Sku sku = null;
        for (Map<String, String> skuMap : skus) {
            sku = new Sku();
            sku.setCreateTime(System.currentTimeMillis());
            sku.setProductId(productId);
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> skuEntry : skuMap.entrySet()) {
                if (!skuEntry.getKey().equals("price")&&!skuEntry.getKey().equals("store")&&!skuEntry.getKey().equals("indexs")){
                    sb.append(skuEntry.getValue());
                }
            }
            sku.setSkuName(sb.toString());
            sku.setPrice(Integer.parseInt(skuMap.get("price")));
            sku.setAvailableStock(Integer.parseInt(skuMap.get("store")));
            sku.setIndexs(skuMap.get("indexs"));
            skuMapper.insert(sku);
        }
    }
}
