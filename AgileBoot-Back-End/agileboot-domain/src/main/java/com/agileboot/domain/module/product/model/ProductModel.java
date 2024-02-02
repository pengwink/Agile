package com.agileboot.domain.module.product.model;

import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode;
import com.agileboot.domain.module.product.command.AddProductCommand;
import com.agileboot.domain.module.product.command.UpdateProductCommand;
import com.agileboot.domain.module.product.db.ProductEntity;
import com.agileboot.domain.module.product.db.ProductService;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author jinani7
 * @ClassName ProductModel
 * @description: TODO
 * @date 2024年01月26日
 * @version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProductModel extends ProductEntity {
    private ProductService productService;

    public ProductModel(ProductEntity entity, ProductService productService) {
        if (entity != null) {
            BeanUtil.copyProperties(entity, this);
        }
        this.productService = productService;
    }

    public void loadAddProductCommand(AddProductCommand command) {
        if (command != null) {
            BeanUtil.copyProperties(command, this, "userId");
        }
    }

    public void loadUpdateProductCommand(UpdateProductCommand command) {
        if (command != null) {
            loadAddProductCommand(command);
        }
    }

    public void checkEncodingIsUnique() {
        if (productService.isEncodingDuplicated(getEncoding())) {
            throw new ApiException(ErrorCode.Business.PRODUCT_ENCODING_IS_NOT_UNIQUE);
        }
    }

    public ProductModel(ProductService productService) {
        this.productService=productService;
    }
}
