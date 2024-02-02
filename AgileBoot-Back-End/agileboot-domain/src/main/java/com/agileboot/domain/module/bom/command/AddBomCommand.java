package com.agileboot.domain.module.bom.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class AddBomCommand {

    @NotBlank(message = "bom表不能为空")
    protected String bomName;

}
