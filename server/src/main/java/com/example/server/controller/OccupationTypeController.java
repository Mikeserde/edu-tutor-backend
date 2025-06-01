package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.mapper.OccupationTypeMapper;
import com.example.server.model.OccupationType;
import com.example.server.service.OccupationTypeService;
import com.example.server.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/occupation-types")
@Tag(name = "职业类型管理", description = "职业类型管理接口")
@CrossOrigin
public class OccupationTypeController {

    @Resource
    private OccupationTypeService occupationTypeService;

    @Operation(summary = "创建职业类型")
    @PostMapping
    public Result createOccupationType(@Valid @RequestBody OccupationType occupationType) {
        boolean saved = occupationTypeService.save(occupationType);
        return saved ?
                Result.ok().message("创建成功").data("data", occupationType) :
                Result.error().message("创建失败");
    }

    @Operation(summary = "根据ID更新职业类型")
    @PutMapping("/{id}")
    public Result updateOccupationType(
            @PathVariable @NotNull(message = "ID不能为空") Integer id,
            @Valid @RequestBody OccupationType occupationType) {
        occupationType.setOccupationTypeId(id);
        boolean updated = occupationTypeService.updateById(occupationType);
        return updated ?
                Result.ok().message("更新成功").data("data", occupationType) :
                Result.error().message("更新失败");
    }

    @GetMapping("/list-all")
    public Result getAllOccupationTypes() {
        List<OccupationType> list = occupationTypeService.list();
        return Result.ok().data("data", list);
    }

    @Operation(summary = "根据ID查询职业类型")
    @GetMapping("/{id}")
    public Result getOccupationTypeById(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        OccupationType occupationType = occupationTypeService.getById(id);
        return occupationType != null ?
                Result.ok().data("data", occupationType) :
                Result.error().message("职业类型不存在");
    }

    @Operation(summary = "分页查询职业类型列表")
    @GetMapping
    public Result getOccupationTypes(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {

        Page<OccupationType> page = new Page<>(pageNum, pageSize);
        QueryWrapper<OccupationType> wrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like("Name", name);
        }
        return Result.ok().data("page", occupationTypeService.page(page, wrapper));
    }

    @Operation(summary = "根据ID删除职业类型")
    @DeleteMapping("/{id}")
    public Result deleteOccupationType(
            @PathVariable @NotNull(message = "ID不能为空") Integer id) {
        boolean removed = occupationTypeService.removeById(id);
        return removed ?
                Result.ok().message("删除成功") :
                Result.error().message("删除失败");
    }
}
