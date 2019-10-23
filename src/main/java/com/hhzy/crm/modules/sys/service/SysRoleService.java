package com.hhzy.crm.modules.sys.service;

import com.hhzy.crm.modules.sys.entity.SysRole;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:31
 * @Description:
 */
public interface SysRoleService {


    List<SysRole> list();


    SysRole selectById(Long roleId);


    void save(SysRole sysRole);

    void update(SysRole sysRole);

    void deleteBatch(List<Long> roleIds);


}
