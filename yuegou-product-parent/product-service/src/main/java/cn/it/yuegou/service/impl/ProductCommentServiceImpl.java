package cn.it.yuegou.service.impl;

import cn.it.yuegou.domain.ProductComment;
import cn.it.yuegou.mapper.ProductCommentMapper;
import cn.it.yuegou.service.IProductCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品评价 服务实现类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-17
 */
@Service
public class ProductCommentServiceImpl extends ServiceImpl<ProductCommentMapper, ProductComment> implements IProductCommentService {

}
