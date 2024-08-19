package com.yql.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yql.common.vo.Result;
import com.yql.sys.entity.Role;
import com.yql.sys.entity.User;
import com.yql.sys.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yql
 * @since 2024-08-15
 */
@Api(tags = "角色接口列表")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService iRoleService;

    @ApiOperation("条件查询")
    @GetMapping("/list")
    public Result<Map<String,Object>> selectRoleList(@RequestParam(value = "roleName",required = false) String roleName,
                                                     @RequestParam(value = "pageNo")Integer pageNo,
                                                     @RequestParam(value = "pageSize")Integer pageSize){
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName,roleName);
        lambdaQueryWrapper.orderByDesc(Role::getRoleId);
        Page<Role> page = new Page<>(pageNo,pageSize);
        Page<Role> page1 = iRoleService.page(page, lambdaQueryWrapper);
        Map<String,Object> data = new HashMap<>();
        data.put("total",page1.getTotal());
        data.put("rows",page1.getRecords());
        return Result.success(data);
    }

    @ApiOperation("根据id查询")
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id){
        Role role = iRoleService.getRoleById(id);
        //System.out.println(role);
        return Result.success(role);
    }
    @ApiOperation("新增角色")
    @PostMapping
    public Result<?> saveRole(@RequestBody Role role){
        iRoleService.addRole(role);
        return Result.success("保存角色成功");
    }
    @ApiOperation("修改角色")
    @PutMapping
    public Result<?> updateRole(@RequestBody Role role){
        iRoleService.updateRole(role);
        return Result.success("修改角色成功");
    }
    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public Result<?> deleteRole(@PathVariable("id") Integer id){
        iRoleService.deleteRole(id);
        return Result.success("删除角色成功");
    }
    @ApiOperation("查询所有角色")
    @GetMapping("/all")
    public Result<List<Role>> getAllRole(){
        List<Role> list = iRoleService.list();
        return Result.success(list);
    }
}
