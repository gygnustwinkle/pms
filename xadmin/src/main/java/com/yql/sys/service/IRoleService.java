package com.yql.sys.service;

import com.yql.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yql
 * @since 2024-08-15
 */
public interface IRoleService extends IService<Role> {


    void addRole(Role role);

    Role getRoleById(Integer id);

    void updateRole(Role role);

    void deleteRole(Integer id);
}
