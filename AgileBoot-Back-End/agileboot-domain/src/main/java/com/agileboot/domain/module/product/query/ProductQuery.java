package com.agileboot.domain.module.product.query;

import com.agileboot.common.core.page.AbstractPageQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.apache.commons.math3.stat.descriptive.summary.Product;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jinani7
 * @ClassName productQuery
 * @description: TODO
 * @date 2024年01月25日
 * @version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductQuery<T> extends AbstractPageQuery<Product> {

    protected  Long productId;
    protected  String productName;
    protected  String encoding;

    @Override
    public QueryWrapper<Product> addQueryCondition() {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>()
                .like(StrUtil.isNotEmpty(productName), "product_name", productName)
                .like(StrUtil.isNotEmpty(encoding), "encoding", encoding);
        // 当前端没有选择排序字段时，则使用post_sort字段升序排序（在父类AbstractQuery中默认为升序）
        if (StrUtil.isEmpty(this.getOrderColumn())) {
            this.setOrderColumn("product_id");
        }
        this.setTimeRangeColumn("create_time");

        return queryWrapper;
    }
}
