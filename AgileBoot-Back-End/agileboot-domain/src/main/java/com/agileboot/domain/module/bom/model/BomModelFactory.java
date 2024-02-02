package com.agileboot.domain.module.bom.model;

import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode;
import com.agileboot.domain.module.bom.db.BomEntity;
import com.agileboot.domain.module.bom.db.BomService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * @author jinani7
 */
@Component
@RequiredArgsConstructor
public class BomModelFactory {

    private final BomService bomService;

    public BomModel loadById(Long bomId){
        BomEntity byId = bomService.getById(bomId);
        if(byId == null){
            throw new ApiException(ErrorCode.Business.COMMON_OBJECT_NOT_FOUND, bomId, "BOM");
        }
        return new BomModel(byId,bomService);
    }

    public BomModel create(){
        return new BomModel(bomService);
    }

}
