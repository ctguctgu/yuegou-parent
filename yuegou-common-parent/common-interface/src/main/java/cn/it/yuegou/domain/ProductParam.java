package cn.it.yuegou.domain;

import lombok.Data;

/**
 * @author yinjunchi
 */
@Data
public class ProductParam {

    private String keyword;

    private Long productTypeId;

    private Long brandId;

    private Integer minPrice;

    private Integer maxPrice;

    private String sortField;

    private String sortType;

    private Integer page;

    private Integer rows;

}