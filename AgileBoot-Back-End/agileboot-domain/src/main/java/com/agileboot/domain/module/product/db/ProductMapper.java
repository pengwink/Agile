package com.agileboot.domain.module.product.db;

import com.agileboot.domain.module.product.dto.ProductDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jinani7
 * @ClassName ProductMapper
 * @description: TODO
 * @date 2024年01月25日
 * @version: 1.0
 */
public interface ProductMapper extends BaseMapper<ProductEntity> {

    @Select("SELECT encoding,product_name,coding_area,model,specification\n" +
            "FROM `inventory_product` t\n" +
            "WHERE 1=1\n" +
            "GROUP BY t.product_name,t.encoding")
    List<ProductDTO> selectProductList();
}