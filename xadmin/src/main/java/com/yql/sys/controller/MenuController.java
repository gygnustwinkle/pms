package com.yql.sys.controller;

import com.yql.common.vo.Result;
import com.yql.sys.entity.Menu;
import com.yql.sys.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yql
 * @since 2024-08-15
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "导航菜单接口列表")
public class MenuController {
    @Autowired
    private IMenuService iMenuService;

    @ApiOperation("查询所有数据")
    @GetMapping
    public Result<List<Menu>> getAllMenu(){
        List<Menu> menuList = iMenuService.getAllMenu();
        return Result.success(menuList);
    }
}
