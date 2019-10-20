package cn.it.yuegou.controller;

import cn.it.yuegou.domain.Specification;
import cn.it.yuegou.service.IProductService;
import cn.it.yuegou.domain.Product;
import cn.it.yuegou.query.ProductQuery;
import cn.it.yuegou.util.AjaxResult;
import cn.it.yuegou.util.PageList;
import cn.it.yuegou.util.StrUtils;
import cn.it.yuegou.vo.SkusVo;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    public IProductService productService;

    /**
    * 保存和修改公用的
    * @param product  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Product product){
        try {
            if(product.getId()!=null){
                productService.updateById(product);
            }else{
                productService.save(product);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id){
        try {
            productService.removeById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    @RequestMapping(value="/batchdelete/{ids}",method=RequestMethod.DELETE)
    public AjaxResult batchRemove(@PathVariable("ids") String ids){
        try {
            List<Long> longs = StrUtils.splitStr2LongArr(ids);
            productService.removeByIds(longs);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Product get(@PathVariable("id") Long id)
    {
        return productService.getById(id);
    }

    /**
    * 查看所有
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Product> list(){

        return productService.list(null);
    }

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Product> json(@RequestBody ProductQuery query) {
        return productService.queryPage(query);
    }

    /**
     * 根据商品ID查询显示属性
     * @return
     */
    @GetMapping("/getViewProperties/{productId}")
    public List<Specification> getViewProperties(@PathVariable("productId") Long productId){
        return productService.getViewProperties(productId);
    }

    /**
     * 保存显示属性
     * @param param
     * @return
     */
    @PostMapping("/saveViewProperties")
    public AjaxResult saveViewProperties(@RequestBody Map<String,Object> param){
        Integer integer = (Integer) param.get("productId");
        Long productId = Long.parseLong(integer.toString());
        List<Specification> specifications = (List<Specification>) param.get("viewProperties");
        productService.saveViewProperties(productId,specifications);
        return AjaxResult.me();
    }

    /**
     * 根据商品ID查询SKU属性
     * @return
     */
    @GetMapping("/getSkuProperties/{productId}")
    public List<Specification> getSkuProperties(@PathVariable("productId") Long productId){
        return productService.getSkuProperties(productId);
    }

    /**
     * 保存sku属性
     * @param productId
     * @param skusVo
     * @return
     */
    @PostMapping("/saveSkuProperties")
    public AjaxResult saveSkuProperties(@RequestParam("productId") Long productId, @RequestBody SkusVo skusVo){
        try {
            productService.saveSkuProperties(productId,skusVo.getSkuProperties(),skusVo.getSkus());
            return AjaxResult.me().setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("保存失败："+e.getMessage());
        }
    }
}
