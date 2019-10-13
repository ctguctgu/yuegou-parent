package cn.it.yuegou.service;

import cn.it.yuegou.domain.Brand;
import cn.it.yuegou.query.BrandQuery;
import cn.it.yuegou.util.PageList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 品牌信息 服务类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-12
 */
public interface IBrandService extends IService<Brand> {

    /**
     * 分页查询
     * @param query
     * @return
     */
    PageList<Brand> queryPage(BrandQuery query);
}
