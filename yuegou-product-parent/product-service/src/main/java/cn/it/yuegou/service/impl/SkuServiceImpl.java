package cn.it.yuegou.service.impl;

import cn.it.yuegou.domain.Sku;
import cn.it.yuegou.mapper.SkuMapper;
import cn.it.yuegou.service.ISkuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * SKU 服务实现类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-17
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {
    /**
     * 获取价格
     * @param productId
     * @return
     */
    @Override
    public List<Sku> getPrices(Long productId) {
        return baseMapper.selectList(new QueryWrapper<Sku>().eq("product_id", productId));
    }
}
