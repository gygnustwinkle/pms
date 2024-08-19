package com.yql.sys.service;

import com.yql.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yql
 * @since 2024-08-15
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login(User user);

    Map<String, Object> getUserInfo(String token);

    void logout(String token);

    void saveUser(User user);

    User selectUserById(Integer id);

    void updateUser(User user);

    void deleteUser(Integer id);
}
