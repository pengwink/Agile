package com.agileboot.domain.module.product.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jinani7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateProductCommand extends AddProductCommand{

    @NotNull(message = "产品ID不能为空")
    @Positive
    private Long productId;

}
