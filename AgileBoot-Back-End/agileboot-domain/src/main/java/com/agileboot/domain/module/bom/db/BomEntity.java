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
 * <p>
 *  Bom信息表
 * </p>
 *
 * @author jinan
 * @since 2024-1-31
 */
@Getter
@Setter
@TableName("inventory_bom")
@ApiModel(value = "BomEntity对象", description = "Bom信息表")
public class BomEntity extends BaseEntity<BomEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Bom表ID")
    @TableId(value = "bom_id", type = IdType.AUTO)
    private Long bomId;

    @ApiModelProperty("Bom名称")
    @TableField("bom_name")
    private String bomName;

    @Override
    public Serializable pkVal() {
        return this.bomId;
    }

}
