package cn.it.yuegou.controller;

import cn.it.yuegou.service.IProductTypeService;
import cn.it.yuegou.domain.ProductType;
import cn.it.yuegou.query.ProductTypeQuery;
import cn.it.yuegou.util.AjaxResult;
import cn.it.yuegou.util.PageList;
import cn.it.yuegou.vo.ProductTypeCrumbVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productType")
public class ProductTypeController {
    @Autowired
    public IProductTypeService productTypeService;

    /**
    * 保存和修改公用的
    * @param productType  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody ProductType productType){
        try {
            if(productType.getId()!=null){
                productTypeService.updateById(productType);
            }else{
                productTypeService.save(productType);
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
            productTypeService.removeById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ProductType get(@PathVariable("id") Long id)
    {
        return productTypeService.getById(id);
    }


    /**
    * 查看所有
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<ProductType> list(){

        return productTypeService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<ProductType> json(@RequestBody ProductTypeQuery query)
    {
        Page<ProductType> page = new Page<ProductType>(query.getPageNum(),query.getPageSize());
        IPage<ProductType> ipage = productTypeService.page(page);
        return new PageList<ProductType>(ipage.getTotal(),ipage.getRecords());
    }

    @RequestMapping(value = "/loadTree",method = RequestMethod.POST)
    public List<ProductType> loadTree (){
        return productTypeService.loadTypeTree();
    }

    /**
     * 静态化首页
     */
    @PostMapping("/homepage")
    public AjaxResult StaticPage(){
        try {
            productTypeService.homePage();
            return AjaxResult.me().setMessage("成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("失败！");
        }
    }

    /**
     * 加载类型面包屑
     * @param productTypeId
     * @return
     */
    @GetMapping("/crumb")
    public List<ProductTypeCrumbVo> loadTypeCrumb(@RequestParam("productTypeId") Long productTypeId){
        return productTypeService.loadTypeCrumb(productTypeId);
    }
}
