package cn.it.yuegou.mapper;

import cn.it.yuegou.domain.Product;
import cn.it.yuegou.query.ProductQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-17
 */
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 分页高级查询
     * @param page
     * @param query
     * @return
     */
    IPage<Product> queryPage(Page page, @Param("query") ProductQuery query);

    /**
     * 上架
     * @param ids
     * @param time
     */
    void onSale(@Param("ids") List<Long> ids, @Param("time") long time);

    /**
     * 下架
     * @param ids
     * @param time
     */
    void offSale(@Param("ids") List<Long> ids, @Param("time") long time);
}
