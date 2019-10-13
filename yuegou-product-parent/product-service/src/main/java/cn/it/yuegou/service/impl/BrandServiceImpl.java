package cn.it.yuegou.service.impl;

import cn.it.yuegou.domain.Brand;
import cn.it.yuegou.mapper.BrandMapper;
import cn.it.yuegou.query.BrandQuery;
import cn.it.yuegou.service.IBrandService;
import cn.it.yuegou.util.PageList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
