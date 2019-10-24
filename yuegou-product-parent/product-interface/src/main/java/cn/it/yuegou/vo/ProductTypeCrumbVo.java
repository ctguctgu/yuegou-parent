package cn.it.yuegou.vo;

import cn.it.yuegou.domain.ProductType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinjunchi
 */
@Data
public class ProductTypeCrumbVo {
    /**
     * 当前商品类型
     */
    private ProductType currentType;
    /**
     * 兄弟类型
     */
    private List<ProductType> otherTypes=new ArrayList<>();

}
