package cn.it.yuegou.service.impl;

import cn.it.yuegou.domain.Product;
import cn.it.yuegou.domain.ProductExt;
import cn.it.yuegou.mapper.ProductExtMapper;
import cn.it.yuegou.mapper.ProductMapper;
import cn.it.yuegou.query.ProductQuery;
import cn.it.yuegou.service.IProductService;
import cn.it.yuegou.util.PageList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
