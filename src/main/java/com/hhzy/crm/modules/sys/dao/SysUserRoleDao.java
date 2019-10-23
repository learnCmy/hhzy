package com.hhzy.crm.modules.sys.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.sys.entity.SysRole;
import com.hhzy.crm.modules.sys.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:21
 * @Description:
 */
@Mapper
public interface SysUserRoleDao extends MyMapper<SysUserRole> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    @Select("SELECT b.* FROM sys_user_role a LEFT JOIN  sys_role b on a.role_id=b.role_id WHERE a.user_id=#{userId}")
    List<SysRole> queryRoleList(Long userId);


    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
