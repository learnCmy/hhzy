package com.hhzy.crm.modules.sys.service;

import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.entity.SysUserToken;

import java.util.Set;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 17:12
 * @Description:
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    SysUserToken queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUser queryUser(Long userId);

}
