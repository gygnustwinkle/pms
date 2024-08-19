package com.yql.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yql.sys.entity.Menu;
import com.yql.sys.mapper.MenuMapper;
import com.yql.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> getAllMenu() {
        //一级菜单
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId,0);
        List<Menu> menuList = this.list(lambdaQueryWrapper);
        //填充子菜单
        setMenuChildren(menuList);
        return menuList;
    }
    private void setMenuChildren(List<Menu> menuList) {
        if(null != menuList){
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> subLambdaQueryWrapper = new LambdaQueryWrapper<>();
                subLambdaQueryWrapper.eq(Menu::getParentId,menu.getMenuId());
                List<Menu> subMenuList = this.list(subLambdaQueryWrapper);
                menu.setChildren(subMenuList);
                //递归
                setMenuChildren(subMenuList);
            }
        }
    }

    @Override
    public List<Menu> getMenuListByUserId(Integer userId) {
        //一级菜单
        List<Menu> menuList = this.baseMapper.getMenuListByUserId(userId, 0);
        //填充子菜单
        System.out.println("一级节点menuList = " + menuList);
        setMenuChildrenByUserId(userId, menuList);
        System.out.println("子节点menuList = " + menuList);
        return menuList;
    }

    private void setMenuChildrenByUserId(Integer userId, List<Menu> menuList) {
        if (null != menuList){
            for (Menu menu : menuList) {
                List<Menu> subMenuList = this.baseMapper.getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(subMenuList);
                //递归
                setMenuChildrenByUserId(userId,subMenuList);
            }
        }
    }


}
