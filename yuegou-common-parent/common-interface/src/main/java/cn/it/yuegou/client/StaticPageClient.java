package cn.it.yuegou.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yinjunchi
 */
@FeignClient("YUEGOU-COMMON")
public interface StaticPageClient {
    /**
     * 公共的接口-使用模板技术静态化页面
     * @param templatePath
     * @param targetPath
     * @param model
     */
    @PostMapping("/page")
    public void generatePage(@RequestParam(value = "templatePath")String templatePath, @RequestParam(value = "targetPath")String targetPath, @RequestBody Object model);
}
