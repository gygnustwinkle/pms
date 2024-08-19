package com.yql.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yql.sys.entity.Role;
import com.yql.sys.entity.RoleMenu;
import com.yql.sys.mapper.RoleMapper;
import com.yql.sys.mapper.RoleMenuMapper;
import com.yql.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yql
 * @since 2024-08-15
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public void addRole(Role role) {
        //写入角色
        role.setDeleted(0);
        this.baseMapper.insert(role);
        //写入角色菜单映射
        if (null != role.getMenuIdList()){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId,0));
            }
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = this.baseMapper.selectById(id);
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(id);
        role.setMenuIdList(menuIdList);
        //System.out.println(menuIdList);
        return role;
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        System.out.println(role.getMenuIdList());
        //修改角色表
        this.baseMapper.updateById(role);
        //删除原有的角色菜单表
        LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoleMenu::getRoleId,role.getRoleId());
        roleMenuMapper.delete(lambdaQueryWrapper);
        //新增角色菜单表
        if (null != role.getMenuIdList()){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId,0));
            }
        }
    }

    @Override
    @Transactional
    public void deleteRole(Integer id) {
        //清除角色表数据
        this.baseMapper.deleteById(id);
        //清除角色菜单表数据
        LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuMapper.delete(lambdaQueryWrapper);
    }
}
