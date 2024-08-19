package com.yql.sys.service;

import com.yql.sys.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yql
 * @since 2024-08-15
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> getAllMenu();
    List<Menu> getMenuListByUserId(Integer userId);
}
