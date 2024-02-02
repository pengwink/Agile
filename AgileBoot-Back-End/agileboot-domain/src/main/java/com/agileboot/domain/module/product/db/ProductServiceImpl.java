package com.agileboot.domain.module.product.db;

import com.agileboot.domain.module.product.dto.ProductDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jinani7
 * @ClassName ProductServiceImpl
 * @description: TODO
 * @date 2024年01月25日
 * @version: 1.0
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductEntity> implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public List<ProductDTO> querySelect() {
        return productMapper.selectProductList();
    }

    @Override
    public boolean isEncodingDuplicated(String encoding) {
        QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("encoding", encoding);
        return this.baseMapper.exists(queryWrapper);
    }
}
