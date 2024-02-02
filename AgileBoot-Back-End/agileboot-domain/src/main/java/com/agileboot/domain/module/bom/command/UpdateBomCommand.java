package com.agileboot.domain.module.bom.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateBomCommand extends AddBomCommand{

    @NotNull(message = "bomId不能为空")
    @Positive
    private Long bomId;
}
