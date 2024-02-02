package com.agileboot.domain.module.product.db;

import com.agileboot.domain.module.product.dto.ProductDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jinani7
 * @ClassName IProductService
 * @description: TODO
 * @date 2023年09月15日
 * @version: 1.0
 */
public interface ProductService extends IService<ProductEntity> {
    List<ProductDTO> querySelect();

    boolean isEncodingDuplicated(String encoding);
//    public BasePage<ProductDTO> queryPage(ProductQuery query);

//    public List<ProductVO> queryList(ProductQuery query);
//
//    void exportExcel(Map<String, Object> param, HttpServletResponse response);
//
//    public R importExcel(MultipartFile file);
//
//    public List<ProductVO> productList(String query);
}
