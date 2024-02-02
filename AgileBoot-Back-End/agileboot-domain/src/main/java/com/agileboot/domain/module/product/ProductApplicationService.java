package com.agileboot.domain.module.product;

import com.agileboot.common.core.page.PageDTO;
import com.agileboot.domain.common.command.BulkOperationCommand;
import com.agileboot.domain.module.product.command.AddProductCommand;
import com.agileboot.domain.module.product.command.UpdateProductCommand;
import com.agileboot.domain.module.product.dto.ProductDTO;
import com.agileboot.domain.module.product.db.ProductEntity;
import com.agileboot.domain.module.product.model.ProductModel;
import com.agileboot.domain.module.product.model.ProductModelFactory;
import com.agileboot.domain.module.product.query.ProductQuery;
import com.agileboot.domain.module.product.db.ProductService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductApplicationService {

    private final ProductService productService;
    private final ProductModelFactory productModelFactory;

    public PageDTO<ProductDTO> queryPage(ProductQuery query) {
        Page<ProductEntity> page = productService.page(query.toPage(),query.toQueryWrapper());
        List<ProductDTO> records = page.getRecords().stream().map(ProductDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }

    public List<ProductDTO> querySelect() {
        return productService.querySelect();
    }

    public void addProduct(AddProductCommand command) {
        command.setEndingNumber(command.getStorageNumber());
        ProductModel productModel = new ProductModel(productService);
        productModel.checkEncodingIsUnique();
        productModel.loadAddProductCommand(command);
        productModel.insert();
    }

    public List<ProductDTO> getProductListAll(ProductQuery query) {
        List<ProductEntity> all = productService.list(query.toQueryWrapper());
        List<ProductDTO> records = all.stream().map(ProductDTO::new).collect(Collectors.toList());
        return records;
    }

    public void updateProduct(UpdateProductCommand updateCommand) {
        ProductModel productModel = productModelFactory.loadById(updateCommand.getProductId());
        productModel.loadUpdateProductCommand(updateCommand);
        productModel.updateById();
    }

    public void deleteProduct(BulkOperationCommand<Long> deleteCommand) {
        for (Long id : deleteCommand.getIds()){
            ProductModel productModel = productModelFactory.loadById(id);
            productModel.deleteById();
        }
        productService.removeBatchByIds(deleteCommand.getIds());
    }
}
