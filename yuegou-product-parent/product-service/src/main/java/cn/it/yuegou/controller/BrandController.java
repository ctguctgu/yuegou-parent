package cn.it.yuegou.controller;

import cn.it.yuegou.service.IBrandService;
import cn.it.yuegou.domain.Brand;
import cn.it.yuegou.query.BrandQuery;
import cn.it.yuegou.util.AjaxResult;
import cn.it.yuegou.util.LetterUtil;
import cn.it.yuegou.util.PageList;
import cn.it.yuegou.util.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yinjunchi
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    public IBrandService brandService;

    /**
    * 保存和修改公用的
    * @param brand  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Brand brand){
        brand.setFirstLetter(LetterUtil.getFirstLetter(brand.getName()).toUpperCase());
        try {
            if(brand.getId()!=null){
                Brand byId = brandService.getById(brand.getId());
                brand.setCreateTime(byId.getCreateTime());
                brand.setUpdateTime(System.currentTimeMillis());
                brandService.updateById(brand);
            }else{
                brand.setCreateTime(System.currentTimeMillis());
                brandService.save(brand);
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
            brandService.removeById(id);
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
            brandService.removeByIds(longs);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Brand get(@PathVariable("id") Long id)
    {
        return brandService.getById(id);
    }

    /**
    * 查看所有
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Brand> list(){

        return brandService.list(null);
    }

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Brand> json(@RequestBody BrandQuery query) {
        return brandService.queryPage(query);
    }
}
