package cn.it.yuegou.controller;

import cn.it.yuegou.util.VelocityUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author yinjunchi
 */
@RestController
public class StaticPageController {

    /**
     * 公共的接口-使用模板技术静态化页面
     * @param templatePath
     * @param targetPath
     * @param model
     */
    @PostMapping("/page")
    public void generatePage(@RequestParam(value = "templatePath")String templatePath,@RequestParam(value = "targetPath")String targetPath,@RequestBody Object model){
        VelocityUtils.staticByTemplate(model, templatePath,targetPath);
    }
}
