package com.agileboot.domain.module.product.dto;

import com.agileboot.common.annotation.ExcelColumn;
import com.agileboot.domain.module.product.db.ProductEntity;

import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO{

    public ProductDTO(ProductEntity productEntity) {
        if (productEntity != null){
            BeanUtil.copyProperties(productEntity, this);
        }
    }

    private Long productId;

    @ExcelColumn(name = "编码")
    private String encoding;

    @ExcelColumn(name = "品名")
    private String productName;

    @ExcelColumn(name ="编码区域")
    private String codingArea;

    @ExcelColumn(name = "类型")
    private String type;

    @ExcelColumn(name = "型号")
    private String model;

    @ExcelColumn(name = "规格")
    private String specification;

    @ExcelColumn(name = "区域")
    private String region;

    @ExcelColumn(name = "货位")
    private String location;

    @ExcelColumn(name = "期初数量")
    private BigDecimal openingNumber;

    @ExcelColumn(name = "期末数量")
    private BigDecimal endingNumber;

    @ExcelColumn(name = "入库数量")
    private BigDecimal storageNumber;

    @ExcelColumn(name = "出库数量")
    private BigDecimal outboundNumber;

    @ExcelColumn(name = "库存预警")
    private BigDecimal warningNumber;

    @ExcelColumn(name = "单位")
    private String unit;

    @ExcelColumn(name = "备注")
    private String remark;

    private Date createTime;


}
