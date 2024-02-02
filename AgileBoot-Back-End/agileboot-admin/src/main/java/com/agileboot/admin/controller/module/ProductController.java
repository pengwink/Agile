package com.agileboot.admin.controller.module;

import com.agileboot.admin.customize.aop.accessLog.AccessLog;
import com.agileboot.common.core.base.BaseController;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.core.page.PageDTO;
import com.agileboot.common.enums.common.BusinessTypeEnum;
import com.agileboot.common.utils.poi.CustomExcelUtil;
import com.agileboot.domain.common.command.BulkOperationCommand;
import com.agileboot.domain.module.product.command.AddProductCommand;
import com.agileboot.domain.module.product.command.UpdateProductCommand;
import com.agileboot.domain.module.product.dto.ProductDTO;
import com.agileboot.domain.module.product.query.ProductQuery;
import com.agileboot.domain.module.product.ProductApplicationService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import cn.hutool.core.collection.ListUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 产品信息管理
 *
 * @author: pengwink
 */
@Tag(name = "库存：产品管理")
@RestController
@RequestMapping("/inventory/product")
@Validated
@RequiredArgsConstructor
public class ProductController extends BaseController {
    private final ProductApplicationService productApplicationService;

    /**
     * 产品列表查询
     */
    @Operation(summary = "产品列表")
    @PreAuthorize("@permission.has('inventory:product:list')")
    @GetMapping("/list")
    public ResponseDTO<PageDTO<ProductDTO>> query(HttpServletRequest request, @ParameterObject ProductQuery query) {
        PageDTO<ProductDTO> pageDTO = productApplicationService.queryPage(query);
        return ResponseDTO.ok(pageDTO);
    }

    /**
     * 产品选择框数据查询
     */
    @Operation(summary = "产品选择数据")
    @PreAuthorize("@permission.has('inventory:product:select')")
    @GetMapping("/select")
    public ResponseDTO<Map<String, List>> querySelect(HttpServletRequest request) {
        List<ProductDTO> res = productApplicationService.querySelect();
        Map<String,List> mapList = new HashMap<>();
        List<Map<String, String>> uniqueProductNames = new ArrayList<>();
        Set uniqueProduct = new HashSet();
        for (ProductDTO item : res) {
            String productName = item.getProductName();
            if (!uniqueProductNames.stream().anyMatch(map -> map.get("productName").equals(productName))) {
                Map<String, String> productMap = new HashMap<>();
                productMap.put("productName", productName);
                uniqueProductNames.add(productMap);
            }
        }
        List list = res.stream().map(item->{
            Map<String, Object> map = new HashMap<>();
            map.put("encoding", item.getEncoding());
            return map;
        }).collect(Collectors.toList());
        mapList.put("encoding",list);
        mapList.put("product",uniqueProductNames);
        return ResponseDTO.ok(mapList);
    }

    /**
     * 产品列表导入
     * @param file
     */
    @Operation(summary = "产品列表导入")
    @AccessLog(title = "产品管理", businessType = BusinessTypeEnum.IMPORT)
    @PreAuthorize("@permission.has('inventory:product:import')")
    @PostMapping("/excel")
    public ResponseDTO<Void> importProductByExcel(MultipartFile file) {
        List<AddProductCommand> commands = CustomExcelUtil.readFromRequest(AddProductCommand.class, file);

        for (AddProductCommand command : commands) {
            productApplicationService.addProduct(command);
        }
        return ResponseDTO.ok();
    }

    /**
     * 产品导入excel下载
     */
    @Operation(summary = "产品导入excel下载")
    @GetMapping("/excelTemplate")
    public void downloadExcelTemplate(HttpServletResponse response) {
        CustomExcelUtil.writeToResponse(ListUtil.toList(new AddProductCommand()), AddProductCommand.class, response);
    }

    /**
     * 产品列表导出
     */
    @Operation(summary = "产品列表导出")
    @AccessLog(title = "产品管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@permission.has('inventory:product:export')")
    @GetMapping("/excel")
    public void export(HttpServletResponse response, ProductQuery query) {
        List<ProductDTO> all = productApplicationService.getProductListAll(query);
        CustomExcelUtil.writeToResponse(all, ProductDTO.class, response);
    }

    /**
     * 新增产品
     */
    @Operation(summary = "添加产品")
    @PreAuthorize("@permission.has('inventory:product:add')")
    @AccessLog(title = "产品管理", businessType = BusinessTypeEnum.ADD)
    @PostMapping
    public ResponseDTO<Void> add(@RequestBody AddProductCommand addCommand) {
        productApplicationService.addProduct(addCommand);
        return ResponseDTO.ok();
    }

    /**
     * 修改产品
     */
    @Operation(summary = "修改产品")
    @PreAuthorize("@permission.has('inventory:product:edit')")
    @AccessLog(title = "产品管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping
    public ResponseDTO<Void> edit(@RequestBody UpdateProductCommand updateCommand) {
        productApplicationService.updateProduct(updateCommand);
        return ResponseDTO.ok();
    }

    /**
     * 删除产品
     */
    @Operation(summary = "删除产品")
    @PreAuthorize("@permission.has('inventory:product:remove')")
    @AccessLog(title = "产品管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping
    public ResponseDTO<Void> remove(@RequestParam @NotNull @NotEmpty List<Long> ids) {
        productApplicationService.deleteProduct(new BulkOperationCommand<>(ids));
        return ResponseDTO.ok();
    }
}
