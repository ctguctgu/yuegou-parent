package cn.it.yuegou.controller;

import cn.it.yuegou.domain.ProductDoc;
import cn.it.yuegou.repository.ProductDocRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}
