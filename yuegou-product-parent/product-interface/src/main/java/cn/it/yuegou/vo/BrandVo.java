package cn.it.yuegou.vo;

import cn.it.yuegou.domain.Brand;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author yinjunchi
 */
@Data
public class BrandVo {
    private List<Brand> brands=new ArrayList<>();
    private Set<String> letters=new TreeSet<>();
}
