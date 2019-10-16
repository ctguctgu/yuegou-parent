package cn.it.yuegou.service.impl;

import cn.it.yuegou.client.RedisClient;
import cn.it.yuegou.client.StaticPageClient;
import cn.it.yuegou.domain.ProductType;
import cn.it.yuegou.mapper.ProductTypeMapper;
import cn.it.yuegou.service.IProductTypeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.xdevapi.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品目录 服务实现类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-12
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements IProductTypeService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private StaticPageClient staticPageClient;

    @Override
    public List<ProductType> loadTypeTree() {
        String inRedis = redisClient.get("productTypes");
        System.out.println("查询redis");
        List<ProductType> productTypes = null;
        if (StringUtils.isEmpty(inRedis)){
            synchronized (this){
                inRedis = redisClient.get("productTypes");
                if (StringUtils.isEmpty(inRedis)){
                    productTypes = getTreeDataLoop();
                    System.out.println("查询数据库");
                    String s = JSON.toJSONString(productTypes);
                    redisClient.set("productTypes", s);
                    return productTypes;
                }else {
                    productTypes = JSONArray.parseArray(inRedis, ProductType.class);
                }
                return productTypes;
            }
        }
        productTypes = JSONArray.parseArray(inRedis, ProductType.class);
        return productTypes;
    }

    @Override
    public void homePage() {
        //先根据product.type.vm模板生成product.type.vm.html
        //模板路径
        String templatePath = "D:\\software\\IdeaProjects\\yuegou-parent\\yuegou-product-parent\\product-service\\src\\main\\resources\\template\\product.type.vm";
        String targetPath = "D:\\software\\IdeaProjects\\yuegou-parent\\yuegou-product-parent\\product-service\\src\\main\\resources\\template\\product.type.vm.html";
        List<ProductType> productTypes = loadTypeTree();
        staticPageClient.generatePage(templatePath,targetPath,productTypes);
        //再根据home.vm生成html.html
        templatePath = "D:\\software\\IdeaProjects\\yuegou-parent\\yuegou-product-parent\\product-service\\src\\main\\resources\\template\\home.vm";
        targetPath = "D:\\software\\IdeaProjects\\yuegou-web-parent\\ecommerce\\home.html";
        Map<String,Object> model = new HashMap<>();
        model.put("staticRoot","D:\\software\\IdeaProjects\\yuegou-parent\\yuegou-product-parent\\product-service\\src\\main\\resources\\");
        staticPageClient.generatePage(templatePath,targetPath,model);
    }

    @Override
    public boolean save(ProductType entity) {
        boolean save = super.save(entity);
        synchronizedOption();
        return save;
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean removeById = super.removeById(id);
        synchronizedOption();
        return removeById;
    }

    @Override
    public boolean updateById(ProductType entity) {
        boolean updateById = super.updateById(entity);
        synchronizedOption();
        return updateById;
    }

    /**
     * 增删改的同步操作
     */
    private void synchronizedOption(){
        List<ProductType> productTypes = getTreeDataLoop();
        String s = JSON.toJSONString(productTypes);
        redisClient.set("productTypes", s);
        //生成home.html静态页面
        homePage();
        System.out.println("同步操作~");
    }

    private List<ProductType> getTreeDataLoop(){
        //先查询所有
        List<ProductType> allProductTypes = baseMapper.selectList(null);
        //再组装数据
        List<ProductType> firstLevelTypes = new ArrayList<>();
        Map<Long,ProductType> productTypeMap = new HashMap<>();
        //将所有的productType存入map中
        for (ProductType productType : allProductTypes) {
            productTypeMap.put(productType.getId(),productType);
        }
        //再循环组装数据
        for (ProductType productType : allProductTypes) {
            //如果是一级
            if(productType.getPid()==0){
                firstLevelTypes.add(productType);
            }else{
                //如果不是一级
                ProductType parent = productTypeMap.get(productType.getPid());
                parent.getChildren().add(productType);
            }
        }
        return firstLevelTypes;
    }
}
