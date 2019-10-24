package cn.it.yuegou.service.impl;

import cn.it.yuegou.client.EsProductClient;
import cn.it.yuegou.client.StaticPageClient;
import cn.it.yuegou.domain.*;
import cn.it.yuegou.mapper.*;
import cn.it.yuegou.query.ProductQuery;
import cn.it.yuegou.service.IProductService;
import cn.it.yuegou.service.IProductTypeService;
import cn.it.yuegou.util.PageList;
import cn.it.yuegou.vo.ProductTypeCrumbVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author yinjunchi
 * @since 2019-10-17
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductExtMapper productExtMapper;
    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private EsProductClient esProductClient;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private IProductTypeService typeService;
    @Autowired
    private StaticPageClient pageClient;

    @Override
    @Transactional
    public boolean save(Product product) {
        product.setCreateTime(System.currentTimeMillis());
        boolean save = super.save(product);
        ProductExt productExt = product.getExt();
        productExt.setProductId(product.getId());
        productExtMapper.insert(productExt);
        return save;
    }

    @Override
    public boolean updateById(Product product) {
        product.setUpdateTime(System.currentTimeMillis());
        boolean updateById = super.updateById(product);
        productExtMapper.updateById(product.getExt());
        return updateById;
    }

    @Override
    public PageList<Product> queryPage(ProductQuery query) {
        IPage<Product> brandIPage = baseMapper.queryPage(new Page(query.getPageNum(), query.getPageSize()), query);
        PageList<Product> list = new PageList<>(brandIPage.getTotal(), brandIPage.getRecords());
        return list;
    }

    @Override
    public List<Specification> getViewProperties(Long productId) {
        List<Specification> specifications = null;
        Product product = baseMapper.selectById(productId);
        String viewProperties = product.getViewProperties();
        if (StringUtils.isEmpty(viewProperties)){
            specifications = specificationMapper.selectList(new QueryWrapper<Specification>()
                    .eq("product_type_id", product.getProductTypeId()).eq("isSku", 0));
        }else {
            specifications =JSONArray.parseArray(viewProperties, Specification.class);
        }
        return specifications;
    }

    @Override
    public void saveViewProperties(Long productId, List<Specification> viewProperties) {
        Product product = baseMapper.selectById(productId);
        product.setViewProperties(JSONArray.toJSONString(viewProperties));
        baseMapper.updateById(product);
    }

    @Override
    public List<Specification> getSkuProperties(Long productId) {
        List<Specification> specifications = null;
        Product product = baseMapper.selectById(productId);
        String skuProperties = product.getSkuProperties();
        if (StringUtils.isEmpty(skuProperties)){
            specifications = specificationMapper.selectList(new QueryWrapper<Specification>()
                    .eq("product_type_id", product.getProductTypeId()).eq("isSku", 1));
        }else {
            specifications =JSONArray.parseArray(skuProperties, Specification.class);
        }
        return specifications;
    }

    /**
     * 保存sku属性
     * @param productId
     * @param skuProperties
     * @param skus
     */
    @Override
    public void saveSkuProperties(Long productId, List<Specification> skuProperties, List<Map<String, String>> skus) {
        Product product = baseMapper.selectById(productId);
        product.setSkuProperties(JSONArray.toJSONString(skuProperties));
        baseMapper.updateById(product);
        skuMapper.delete(new QueryWrapper<Sku>().eq("product_id",productId));
        Sku sku = null;
        for (Map<String, String> skuMap : skus) {
            sku = new Sku();
            sku.setCreateTime(System.currentTimeMillis());
            sku.setProductId(productId);
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> skuEntry : skuMap.entrySet()) {
                if (!skuEntry.getKey().equals("price")&&!skuEntry.getKey().equals("store")&&!skuEntry.getKey().equals("indexs")){
                    sb.append(skuEntry.getValue());
                }
            }
            sku.setSkuName(sb.toString());
            sku.setPrice(Integer.parseInt(skuMap.get("price")));
            sku.setAvailableStock(Integer.parseInt(skuMap.get("store")));
            sku.setIndexs(skuMap.get("indexs"));
            skuMapper.insert(sku);
        }
    }

    /**
     * 批量上架
     * @param ids
     */
    @Override
    public void onSale(List<Long> ids) {
        baseMapper.onSale(ids,System.currentTimeMillis());
        List<Product> products = baseMapper.selectBatchIds(ids);
        List<ProductDoc> productDocList = products2Docs(products);
        staticDetailPage(products);
        esProductClient.saveBatch(productDocList);
    }

    /**
     * 商品详情页-页面静态化
     * @param products
     */
    private void staticDetailPage(List<Product> products) {
        for (Product product : products) {
            String templatePath = "D:\\software\\IdeaProjects\\yuegou-parent\\yuegou-product-parent\\product-service\\src\\main\\resources\\template\\product.detail.vm";
            String targetPath = "D:\\software\\IdeaProjects\\yuegou-web-parent\\ecommerce\\detail\\"+product.getId()+".html";
            Map<String,Object> model = new HashMap<>();
            List<ProductTypeCrumbVo> crumbs = typeService.loadTypeCrumb(product.getProductTypeId());
            model.put("crumbs",crumbs);
            model.put("product",product);
            String skuProperties = product.getSkuProperties();
            List<Specification> skus = JSONArray.parseArray(skuProperties, Specification.class);
            model.put("skus",skus);
            String viewProperties = product.getViewProperties();
            List<Specification> views = JSONArray.parseArray(viewProperties, Specification.class);
            model.put("views",views);
            ProductExt productExt = productExtMapper.selectOne(new QueryWrapper<ProductExt>().eq("product_id", product.getId()));
            String richContent = productExt.getRichContent();
            model.put("richContent",richContent);
            String mediasStr = product.getMedias();
            String[] mediasArr = mediasStr.split(",");
            List<List<String>> images = new ArrayList<>();
            for (String media : mediasArr) {
                List<String> oneMedia = new ArrayList<>();
                oneMedia.add("http://172.16.4.153"+media);
                oneMedia.add("http://172.16.4.153"+media);
                oneMedia.add("http://172.16.4.153"+media);
                images.add(oneMedia);
            }
            String medias = JSON.toJSONString(images);
            model.put("medias",medias);
            model.put("skuJSON", product.getSkuProperties());
            pageClient.generatePage(templatePath,targetPath,model);
        }
    }

    private List<ProductDoc> products2Docs(List<Product> products) {
        List<ProductDoc> productDocs = new ArrayList<>();
        for (Product product : products) {
            ProductDoc productDoc = product2Doc(product);
            productDocs.add(productDoc);
        }
        return productDocs;
    }

    private ProductDoc product2Doc(Product product) {
        ProductDoc productDoc = new ProductDoc();
        ProductType productType = productTypeMapper.selectById(product.getProductTypeId());
        Brand brand = brandMapper.selectById(product.getBrandId());

        productDoc.setId(product.getId());
        StringBuilder all = new StringBuilder();
        all.append(product.getName())
                .append(" ")
                .append(product.getSubName())
                .append(" ")
                .append(productType.getName())
                .append(" ")
                .append(brand.getName());
        productDoc.setAll(all.toString());
        productDoc.setProductTypeId(product.getProductTypeId());
        productDoc.setBrandId(product.getBrandId());
        List<Sku> skus = skuMapper.selectList(new QueryWrapper<Sku>().eq("product_id", product.getId()));
        Integer maxPrice=0;
        Integer minPrice=0;
        if (skus!=null&&skus.size()>0){
            minPrice = skus.get(0).getPrice();
        }
        for (Sku sku : skus) {
            if (sku.getPrice()>maxPrice){
                maxPrice=sku.getPrice();
            }
            if (sku.getPrice()<minPrice){
                minPrice=sku.getPrice();
            }
        }
        productDoc.setMaxPrice(maxPrice);
        productDoc.setMinPrice(minPrice);
        productDoc.setSaleCount(product.getSaleCount());
        productDoc.setOnSaleTime(product.getOnSaleTime());
        productDoc.setCommentCount(product.getCommentCount());
        productDoc.setViewCount(product.getViewCount());
        productDoc.setName(product.getName());
        productDoc.setSubName(product.getSubName());
        productDoc.setMedias(product.getMedias());
        productDoc.setViewProperties(product.getViewProperties());
        productDoc.setSkuProperties(product.getSkuProperties());
        return productDoc;
    }

    /**
     * 批量下架
     * @param ids
     */
    @Override
    public void offSale(List<Long> ids) {
        baseMapper.offSale(ids,System.currentTimeMillis());
        esProductClient.deleteBatch(ids);
    }

    /**
     * 商城搜索上架商品
     * @param param
     * @return
     */
    @Override
    public PageList<Product> queryOnSale(ProductParam param) {
        //查询es中的数据
        PageList<ProductDoc> pageList = esProductClient.search(param);

        //封装成PageList
        List<Product> list = new ArrayList<>();
        Product p = null;
        for (ProductDoc doc : pageList.getRows()) {
            p = new Product();
            p.setMedias(doc.getMedias());
            p.setId(doc.getId());
            p.setName(doc.getName());
            p.setSubName(doc.getSubName());
            p.setSaleCount(doc.getSaleCount());
            p.setMaxPrice(doc.getMaxPrice());
            p.setMinPrice(doc.getMinPrice());
            list.add(p);
        }
        return new PageList<>(pageList.getTotal(),list);
    }
}
