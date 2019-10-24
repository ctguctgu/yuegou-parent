package cn.it.yuegou.service.impl;

import cn.it.yuegou.domain.Brand;
import cn.it.yuegou.mapper.BrandMapper;
import cn.it.yuegou.query.BrandQuery;
import cn.it.yuegou.service.IBrandService;
import cn.it.yuegou.util.PageList;
import cn.it.yuegou.vo.BrandVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * 品牌信息 服务实现类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-12
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

    @Override
    public PageList<Brand> queryPage(BrandQuery query) {
        IPage<Brand> brandIPage = baseMapper.queryPage(new Page(query.getPageNum(), query.getPageSize()), query);
        PageList<Brand> list = new PageList<>(brandIPage.getTotal(), brandIPage.getRecords());
        return list;
    }

    @Override
    public BrandVo getBrandVo(Long productTypeId) {
        BrandVo vo = new BrandVo();
        List<Brand> brands = baseMapper.selectList(new QueryWrapper<Brand>().eq("product_type_id", productTypeId));
        vo.setBrands(brands);
        Set<String> letters =  new TreeSet<>();
        for (Brand brand : brands) {
            letters.add(brand.getFirstLetter());
        }
        vo.setLetters(letters);
        return vo;
    }
}
