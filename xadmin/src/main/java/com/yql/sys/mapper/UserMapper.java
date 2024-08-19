package com.yql.sys.mapper;

import com.yql.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yql.sys.entity.vo.UserRoleVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yql
 * @since 2024-08-15
 */
public interface UserMapper extends BaseMapper<User> {


    String[] getRole(Integer id);

}
