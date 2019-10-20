package cn.it.yuegou.vo;

import cn.it.yuegou.domain.Specification;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 接收前端参数
 * @author yinjunchi
 */
@Data
public class SkusVo {
    private List<Specification> skuProperties;

    private List<Map<String,String>> skus;
}
