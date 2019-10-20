package cn.it.yuegou.service;

import cn.it.yuegou.domain.Sku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * SKU 服务类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-17
 */
public interface ISkuService extends IService<Sku> {

    /**
     * 获取价格、库存
     * @param productId
     * @return
     */
    List<Sku> getPrices(Long productId);
}
