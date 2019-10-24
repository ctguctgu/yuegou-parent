package cn.it.yuegou.service;

import cn.it.yuegou.domain.ProductType;
import cn.it.yuegou.vo.ProductTypeCrumbVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品目录 服务类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-12
 */
public interface IProductTypeService extends IService<ProductType> {

    /**
     * 读取商品类型树
     * @return
     */
    List<ProductType> loadTypeTree ();

    /**
     * 生成静态页面方法
     */
    void homePage();

    /**
     * 加载类型面包屑
     * @param productTypeId
     * @return
     */
    List<ProductTypeCrumbVo> loadTypeCrumb(Long productTypeId);
}
