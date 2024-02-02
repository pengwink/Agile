package com.agileboot.domain.module.product.db;

import com.agileboot.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("inventory_product")
@ApiModel(value = "product对象", description = "产品信息表")
public class ProductEntity extends BaseEntity<ProductEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品ID")
    @TableId(value = "product_id",type = IdType.AUTO)
    private Long productId;

    @ApiModelProperty("产品编码")
    @TableField(value="encoding")
    private String encoding;

    @ApiModelProperty("产品名称")
    @TableField(value="product_name")
    private String productName;

    @ApiModelProperty("编码区域")
    @TableField(value = "coding_area")
    private String codingArea;

    @ApiModelProperty("产品型号")
    @TableField(value = "model")
    private String model;

    @ApiModelProperty("产品规格")
    @TableField(value = "specification")
    private String specification;

    @ApiModelProperty("货位")
    @TableField(value = "location")
    private String location;

    @ApiModelProperty("类型")
    @TableField(value = "type")
    private String type;

    @ApiModelProperty("区域")
    @TableField(value = "region")
    private  String region;

    @ApiModelProperty("期初数量")
    @TableField(value = "opening_number")
    private BigDecimal openingNumber;

    @ApiModelProperty("期末数量")
    @TableField(value = "ending_number")
    private BigDecimal endingNumber;

    @ApiModelProperty("入库数量")
    @TableField(value = "storage_number")
    private BigDecimal storageNumber;

    @ApiModelProperty("出库数量")
    @TableField(value = "outbound_number")
    private BigDecimal outboundNumber;

    @ApiModelProperty("产品单位")
    @TableField(value = "unit")
    private String unit;

    @ApiModelProperty("预警库存")
    @TableField(value = "warning_number")
    private BigDecimal warningNumber;
    /**
     * 增加库存
     *
     * @param amount 要增加的库存数量
     */
    public void increaseInventory(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            endingNumber = endingNumber.add(amount);
        } else {
            throw new IllegalArgumentException("增加的库存数量必须大于0");
        }
    }
    /**
     * 减少库存
     *
     * @param amount 要减少的库存数量
     */
    public void decreaseInventory(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            if (endingNumber.compareTo(amount) >= 0) {
                endingNumber = endingNumber.subtract(amount);
            } else {
                throw new IllegalArgumentException("减少的库存数量不能超过当前库存量");
            }
        } else {
            throw new IllegalArgumentException("减少的库存数量必须大于0");
        }
    }
    /**
     * 获取当前库存量
     *
     * @return 当前库存量
     */
    public BigDecimal getInventory() {
        return endingNumber;
    }
}
