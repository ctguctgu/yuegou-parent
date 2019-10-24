package cn.it.yuegou.controller;

import cn.it.yuegou.domain.ProductDoc;
import cn.it.yuegou.domain.ProductParam;
import cn.it.yuegou.repository.ProductDocRepository;
import cn.it.yuegou.util.PageList;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yinjunchi
 */
@RestController
public class EsProductController {
    @Autowired
    private ProductDocRepository repository;
    @PostMapping("/es/saveBatch")
    public void saveBatch(@RequestBody List<ProductDoc> productDocs){
        repository.saveAll(productDocs);
    }
    @PostMapping("/es/deleteBatch")
    public void deleteBatch(@RequestBody List<Long> ids){
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }
    @PostMapping("/es/products")
    public PageList<ProductDoc> search(@RequestBody ProductParam param){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (StringUtils.isNotEmpty(param.getKeyword())){
            boolQueryBuilder.must(new MatchQueryBuilder("all",param.getKeyword()));
        }
        if (param.getProductTypeId()!=null){
            boolQueryBuilder.must(new TermQueryBuilder("productTypeId", param.getProductTypeId()));
        }
        if (param.getBrandId()!=null){
            boolQueryBuilder.must(new TermQueryBuilder("brandId", param.getBrandId()));
        }
        if (param.getMaxPrice()!=null){
            boolQueryBuilder.must(new RangeQueryBuilder("minPrice").lte(param.getMaxPrice()));
        }
        if (param.getMinPrice()!=null){
            boolQueryBuilder.must(new RangeQueryBuilder("maxPrice").lte(param.getMinPrice()));
        }
        builder.withQuery(boolQueryBuilder);
        String sortColumn = "saleCount";
        if (StringUtils.isNotEmpty(param.getSortField())){
            sortColumn = param.getSortField();
        }
        SortOrder sortOrder = SortOrder.DESC;
        if("asc".equals(param.getSortType())){
            sortOrder = SortOrder.ASC;
        }
        builder.withSort(new FieldSortBuilder(sortColumn).order(sortOrder));
        builder.withPageable(PageRequest.of(param.getPage()-1,param.getRows()));
        Page<ProductDoc> productDocs = repository.search(builder.build());
        return new PageList<>(productDocs.getTotalElements(),productDocs.getContent());
    }

}
