package com.agileboot.domain.module.bom;

import com.agileboot.common.core.page.PageDTO;
import com.agileboot.domain.common.command.BulkOperationCommand;
import com.agileboot.domain.module.bom.command.AddBomCommand;
import com.agileboot.domain.module.bom.command.UpdateBomCommand;
import com.agileboot.domain.module.bom.db.BomEntity;
import com.agileboot.domain.module.bom.db.BomService;
import com.agileboot.domain.module.bom.dto.BomDTO;
import com.agileboot.domain.module.bom.model.BomModel;
import com.agileboot.domain.module.bom.model.BomModelFactory;
import com.agileboot.domain.module.bom.query.BomQuery;
import com.agileboot.domain.system.post.model.PostModel;
import com.agileboot.domain.system.post.model.PostModelFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

/**
 * @author jinani7
 */
@Service
@RequiredArgsConstructor
public class BomApplicationService {

    private final BomService bomService;

    private final BomModelFactory bomModelFactory;

    public PageDTO<BomDTO> getBomList(BomQuery query) {
        Page<BomEntity> page = bomService.getBomList(query.toPage(),query.toQueryWrapper());
        List<BomDTO> records = page.getRecords().stream().map(BomDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records,page.getTotal());
    }

    public void addBom(AddBomCommand addCommand) {
        BomModel bomModel = bomModelFactory.create();
        bomModel.loadFromAddCommand(addCommand);
        bomModel.checkBomNameUnique();
        bomModel.insert();
    }

    public void updateBom(UpdateBomCommand updateCommand) {
        BomModel bomModel = bomModelFactory.loadById(updateCommand.getBomId());
        bomModel.loadFromUpdateCommand(updateCommand);
        bomModel.updateById();
    }

    public void deleteBom(BulkOperationCommand<Long> deleteCommand) {
        for (Long id : deleteCommand.getIds()) {
            BomModel bomModel = bomModelFactory.loadById(id);
            //TODO BOM检查是否可以删除
//            bomModel.checkCanBeDelete();
        }

        bomService.removeBatchByIds(deleteCommand.getIds());
    }
}
