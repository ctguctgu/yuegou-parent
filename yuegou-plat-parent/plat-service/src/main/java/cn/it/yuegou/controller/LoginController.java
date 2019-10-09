package cn.it.yuegou.controller;

import cn.it.yuegou.domain.User;
import cn.it.yuegou.util.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录控制类
 * @author yinjunchi
 */
@RestController
@Api(tags = "这是用户登录控制类")
public class LoginController {

    @ApiOperation(value = "这是用户登录接口")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody User user){
        if ("ctgu".equals(user.getUsername()) && "123456".equals(user.getPassword())){
            return AjaxResult.me().setMessage("登录成功").setResult("进来了哦");
        }
        return AjaxResult.me().setSuccess(false).setMessage("登录失败").setResult("别试了，进不来的");
    }
}
