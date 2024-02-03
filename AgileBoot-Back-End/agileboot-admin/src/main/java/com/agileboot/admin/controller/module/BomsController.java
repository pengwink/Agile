package com.agileboot.admin.controller.module;

import com.agileboot.admin.customize.aop.accessLog.AccessLog;
import com.agileboot.common.core.base.BaseController;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.core.page.PageDTO;
import com.agileboot.common.enums.common.BusinessTypeEnum;
import com.agileboot.domain.common.command.BulkOperationCommand;
import com.agileboot.domain.module.bom.BomApplicationService;
import com.agileboot.domain.module.bom.command.AddBomCommand;
import com.agileboot.domain.module.bom.command.UpdateBomCommand;
import com.agileboot.domain.module.bom.dto.BomDTO;
import com.agileboot.domain.module.bom.query.BomQuery;
import com.agileboot.domain.system.notice.command.NoticeAddCommand;
import com.agileboot.domain.system.notice.command.NoticeUpdateCommand;
import com.agileboot.domain.system.post.PostApplicationService;
import com.agileboot.infrastructure.annotations.unrepeatable.Unrepeatable;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author jinani7
 */
@Tag(name = "库存：BOM管理")
@RestController
@RequestMapping("/inventory/boms")
@Validated
@RequiredArgsConstructor
public class BomsController extends BaseController{

    private final BomApplicationService bomApplicationService;

    @Operation(summary = "Bom列表")
    @PreAuthorize("@permission.has('inventory:bom:list')")
    @GetMapping
    public ResponseDTO<PageDTO<BomDTO>> list(HttpServletRequest request, @ParameterObject BomQuery query){
        PageDTO<BomDTO> pageDTO = bomApplicationService.getBomList(query);
        return ResponseDTO.ok(pageDTO);
    }
    /**
     * 新增Bom表
     */
    @Operation(summary = "添加Bom表")
    @PreAuthorize("@permission.has('inventory:bom:add')")
    @AccessLog(title = "Bom表管理", businessType = BusinessTypeEnum.ADD)
    @PostMapping
    public ResponseDTO<Void> add(@RequestBody @NotNull AddBomCommand addCommand) {
        bomApplicationService.addBom(addCommand);
        return ResponseDTO.ok();
    }

    /**
     * 修改Bom表
     */
    @Operation(summary = "修改Bom")
    @PreAuthorize("@permission.has('inventory:bom:edit')")
    @AccessLog(title = "Bom表管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("/{bomId}")
    public ResponseDTO<Void> edit(@PathVariable Long bomId, @RequestBody UpdateBomCommand updateCommand) {
        updateCommand.setBomId(bomId);
        bomApplicationService.updateBom(updateCommand);
        return ResponseDTO.ok();
    }

    /**
     * 删除Bom表
     */
    @Operation(summary = "删除Bom表")
    @PreAuthorize("@permission.has('inventory:bom:remove')")
    @AccessLog(title = "Bom表管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping
    public ResponseDTO<Void> remove(@RequestParam @NotNull @NotEmpty List<Long> ids) {
        bomApplicationService.deleteBom(new BulkOperationCommand<>(ids));
        return ResponseDTO.ok();
    }

}
