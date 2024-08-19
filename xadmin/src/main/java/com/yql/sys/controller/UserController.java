package com.yql.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yql.common.vo.Result;
import com.yql.sys.entity.User;
import com.yql.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Arrays;
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
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口列表")
//@CrossOrigin 解决跨域问题
public class UserController {
    @Resource
    private IUserService iUserService;

    @Resource
    private PasswordEncoder passwordEncoder;
    @Qualifier("reactiveRedisTemplate")
    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;

    @GetMapping("/all")
    public Result<List<User>> findAll(){
        List<User> list = iUserService.list();
        return Result.success(list,"查询成功");
    }
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user){
        Map<String,Object> data = iUserService.login(user);
        if(null != data){
            return Result.success(data);
        }
        return Result.fail(20002,"用户名或密码错误");
    }
    @GetMapping("/info")
    public Result<Map<String,Object>> getUserInfo(String token){
        Map<String,Object> data = iUserService.getUserInfo(token);
        if(null != data){
            return Result.success(data);
        }
        return Result.fail(20003,"用户登录失效，请重新登录");
    }
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        iUserService.logout(token);
        return Result.success(null,"注销成功");
    }

    @GetMapping("/list")
    public Result<Map<String,Object>> selectUserList(@RequestParam(value = "username",required = false) String username,
                                                @RequestParam(value = "phone",required = false) String phone,
                                                @RequestParam(value = "pageNo") Long pageNo,
                                                @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasLength(username),User::getUsername,username);
        lambdaQueryWrapper.eq(StringUtils.hasLength(phone),User::getPhone,phone);
        lambdaQueryWrapper.orderByDesc(User::getId);
        Page<User> page = new Page<>(pageNo,pageSize);
        Page<User> page1 = iUserService.page(page, lambdaQueryWrapper);
        Map<String,Object> data = new HashMap<>();
        data.put("total",page1.getTotal());
        List<User> users = page1.getRecords();
        data.put("rows",users);
        return Result.success(data);
    }
    @PostMapping
    public Result<?> saveUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDeleted(0);
        iUserService.saveUser(user);
        return Result.success("保存成功");
    }
    @GetMapping("/{id}")
    public Result<User> selectUserById(@PathVariable("id") Integer id){
        User user = iUserService.selectUserById(id);
        return Result.success(user);
    }

    @PutMapping
    public Result<?> updateUser(@RequestBody User user){
        user.setPassword(null);//设置为null 默认不修改这个字段的值
        iUserService.updateUser(user);
        return Result.success("修改成功");
    }
    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@PathVariable("id") Integer id){
        iUserService.deleteUser(id);
        return Result.success("删除成功");
    }
}
