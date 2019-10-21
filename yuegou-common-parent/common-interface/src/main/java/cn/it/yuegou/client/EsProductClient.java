package cn.it.yuegou.client;


import cn.it.yuegou.domain.ProductDoc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yinjunchi
 */
@FeignClient(value = "YUEGOU-COMMON")
public interface EsProductClient {
    @PostMapping("/es/saveBatch")
    public void saveBatch(@RequestBody List<ProductDoc> productDocs);
    @PostMapping("/es/deleteBatch")
    public void deleteBatch(@RequestBody List<Long> ids);
}
