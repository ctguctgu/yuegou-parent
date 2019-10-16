package cn.it.yuegou.controller;

import cn.it.yuegou.util.AjaxResult;
import cn.it.yuegou.util.FastDfsApiOpr;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author yinjunchi
 */
@RestController
public class FileController {

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/file")
    public AjaxResult upload(MultipartFile file){
        try {
            byte[] content = file.getBytes();
            String filename = file.getOriginalFilename();
            int index = filename.indexOf(".");
            String s = filename.substring(index + 1);
            String filePath = FastDfsApiOpr.upload(content, s);
            return AjaxResult.me().setMessage("上传成功").setResult(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("上传失败！"+e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param filePath
     * @return
     */
    @DeleteMapping("/file")
    public AjaxResult delete(String filePath){
        try {
            filePath = filePath.substring(1);
            int index = filePath.indexOf("/");
            String group = filePath.substring(0, index);
            String fileName = filePath.substring(index + 1);
            FastDfsApiOpr.delete(group, fileName);
            return AjaxResult.me().setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败"+e.getMessage());
        }
    }
}
