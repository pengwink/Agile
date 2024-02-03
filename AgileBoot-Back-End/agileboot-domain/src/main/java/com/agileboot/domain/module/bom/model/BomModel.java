package com.agileboot.domain.module.bom.model;

import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode;
import com.agileboot.common.exception.error.ErrorCode.Business;
import com.agileboot.domain.module.bom.command.AddBomCommand;
import com.agileboot.domain.module.bom.command.UpdateBomCommand;
import com.agileboot.domain.module.bom.db.BomEntity;
import com.agileboot.domain.module.bom.db.BomService;
import com.agileboot.domain.system.post.command.AddPostCommand;
import com.agileboot.domain.system.post.command.UpdatePostCommand;

import cn.hutool.core.bean.BeanUtil;
import lombok.NoArgsConstructor;

/**
 * @author jinani7
 */
@NoArgsConstructor
public class BomModel extends BomEntity {
    private BomService bomService;

    public BomModel(BomService bomService) {
        this.bomService = bomService;
    }

    public BomModel(BomEntity entity,BomService bomService){
        if(entity!=null){
            BeanUtil.copyProperties(entity,this);
        }
        this.bomService = bomService;
    }

    public void loadFromAddCommand(AddBomCommand addCommand) {
        if (addCommand != null) {
            BeanUtil.copyProperties(addCommand, this, "bomId");
        }
    }


    public void loadFromUpdateCommand(UpdateBomCommand command) {
        if (command != null) {
            loadFromAddCommand(command);
        }
    }

    public void checkBomNameUnique() {
        if (bomService.isBomNameDuplicated(getBomId(), getBomName())) {
            throw new ApiException(ErrorCode.Business.BOM_NAME_IS_NOT_UNIQUE, getBomName());
        }
    }

    public void deleteBomInfo() {
        bomService.deleteBomInfo(getBomId());
    }
}
