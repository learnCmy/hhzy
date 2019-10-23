package com.hhzy.crm.modules.sys.service;

import com.hhzy.crm.modules.sys.entity.SysRole;
import com.hhzy.crm.modules.sys.entity.SysUserRole;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:32
 * @Description:
 */
public interface SysUserRoleService {

    void saveOrUpdate(Long userId, List<Long> roleIdList);

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<SysRole> queryRoleList(Long userId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(List<Long> roleIds);
}
