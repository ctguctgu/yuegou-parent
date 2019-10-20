package cn.it.yuegou.service.impl;

import cn.it.yuegou.domain.Product;
import cn.it.yuegou.domain.ProductExt;
import cn.it.yuegou.domain.Specification;
import cn.it.yuegou.mapper.ProductExtMapper;
import cn.it.yuegou.mapper.ProductMapper;
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
    public void saveViewProperties(Long productId, List<Specification> specifications) {
        Product product = baseMapper.selectById(productId);
        product.setViewProperties(JSONArray.toJSONString(specifications));
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
}
