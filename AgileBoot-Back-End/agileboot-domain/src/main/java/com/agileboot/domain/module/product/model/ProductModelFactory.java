package com.agileboot.domain.module.product.model;

import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode;
import com.agileboot.domain.module.product.db.ProductEntity;
import com.agileboot.domain.module.product.db.ProductService;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * @author jinani7
 */
@Component
@RequiredArgsConstructor
public class ProductModelFactory {

    private final ProductService productService;

    public ProductModel loadById(Long productId) {
        ProductEntity byId = productService.getById(productId);
        if (byId == null) {
            throw new ApiException(ErrorCode.Business.COMMON_OBJECT_NOT_FOUND, productId, "产品");
        }
        return new ProductModel(byId, productService);
    }

}
