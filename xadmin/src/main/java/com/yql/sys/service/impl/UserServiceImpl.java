package com.yql.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yql.common.utils.JwtUtil;
import com.yql.sys.entity.Menu;
import com.yql.sys.entity.User;
import com.yql.sys.entity.UserRole;
import com.yql.sys.mapper.UserMapper;
import com.yql.sys.mapper.UserRoleMapper;
import com.yql.sys.service.IMenuService;
import com.yql.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yql
 * @since 2024-08-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserRoleMapper userRoleMapper;

    //spring security加密对象
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IMenuService iMenuService;

    @Override
    public Map<String, Object> login(User user) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据用户查询user
        userLambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        User loginUser = userMapper.selectOne(userLambdaQueryWrapper);
        //查询到了，则生成token，将用户信息存入redis
        if(null != loginUser && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){
            //用jwt生成token
            //String key = "user:" + UUID.randomUUID();
            //存入redis
            loginUser.setPassword(null);
            //redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);
            //用jwt生成token
            String token = JwtUtil.createToken(loginUser);
            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token",token);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        //根据token从jwt里面获取用户信息
        //Object o = redisTemplate.opsForValue().get(token);
        User loginUser = null;
        try {
            loginUser = JwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null != loginUser){
            //User loginUser = JSON.parseObject(JSON.toJSONString(o), User.class);
            Map<String,Object> data = new HashMap<>();
            data.put("username",loginUser.getUsername());
            data.put("avatar",loginUser.getAvatar());
            //角色
            String[] roles = userMapper.getRole(loginUser.getId());
            data.put("roles",roles);
            //权限列表
            List<Menu> menuList = iMenuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList",menuList);
            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        //redisTemplate.delete(token);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        //新增用户
        this.baseMapper.insert(user);
        //新增角色用户表
        if (null != user.getRoleIdList()){
            for (Integer roleId : user.getRoleIdList()) {
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId,0));
            }
        }
    }

    @Override
    @Transactional
    public User selectUserById(Integer id) {
        //查询用户
        User user = this.baseMapper.selectById(id);
        //查询用户对应的角色
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoleList = userRoleMapper.selectList(lambdaQueryWrapper);
        List<Integer> roleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        user.setRoleIdList(roleIdList);
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        this.baseMapper.updateById(user);
        //清除原有角色
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(lambdaQueryWrapper);
        if(null != user.getRoleIdList()){
            for (Integer roleId : user.getRoleIdList()) {
                userRoleMapper.insert(new UserRole(null,user.getId(),roleId,0));
            }
        }
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        this.baseMapper.deleteById(id);
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId,id);
        userRoleMapper.delete(lambdaQueryWrapper);
    }
}
