package com.hhzy.crm.modules.sys.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.sys.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:09
 * @Description:
 */
@Mapper
public interface SysRoleMenuDao extends MyMapper<SysRoleMenu> {


    /**
     * 根据角色ID，获取菜单ID列表
     */
    @Select("select menu_id from sys_role_menu where role_id = #{roleId}")
    List<Long> queryMenuIdList(@Param("roleId") Long roleId);

    @Select("select menu_id from sys_role_menu where role_id = #{roleId} and all_checked=#{checked} ")
    List<Long> queryMenuIdListByCheck(@Param("roleId") Long roleId,Boolean checked);

}
