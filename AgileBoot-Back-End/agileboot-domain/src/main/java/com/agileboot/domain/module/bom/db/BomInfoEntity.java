package com.agileboot.domain.module.bom.db;

import com.agileboot.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jinani7
 */
@Getter
@Setter
@TableName("inventory_bom_info")
@ApiModel(value = "BomInfo对象", description = "Bom详细信息表")
public class BomInfoEntity extends BaseEntity<BomEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("详情表ID")
    @TableId(value = "info_id", type = IdType.AUTO)
    private Long infoId;

    @ApiModelProperty("产品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("产品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("产品编码")
    @TableField("encoding")
    private String encoding;

    @ApiModelProperty("产品型号")
    @TableField("model")
    private String model;

    @ApiModelProperty("产品规格")
    @TableField("specification")
    private String specification;

    @ApiModelProperty("bom表id")
    @TableField("bom_id")
    private Long bomId;

    @ApiModelProperty("数量")
    @TableField("number")
    private Long number;

    @Override
    public Serializable pkVal() {
        return this.infoId;
    }

}