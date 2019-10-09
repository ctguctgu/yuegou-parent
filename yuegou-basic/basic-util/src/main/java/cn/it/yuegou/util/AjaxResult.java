package cn.it.yuegou.util;

/**
 * Ajax请求结果封装工具类
 * @author yinjunchi
 */
public class AjaxResult {
    private Boolean success = true;
    private String message = "操作成功";
    private Object result;

    private AjaxResult() {
    }

    public static AjaxResult me (){
        return new AjaxResult();
    }

    public Boolean getSuccess() {
        return success;
    }

    public AjaxResult setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AjaxResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public AjaxResult setResult(Object result) {
        this.result = result;
        return this;
    }
}
