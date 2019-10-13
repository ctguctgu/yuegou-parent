package cn.it.yuegou.service.impl;

import cn.it.yuegou.domain.Product;
import cn.it.yuegou.mapper.ProductMapper;
import cn.it.yuegou.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-12
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
