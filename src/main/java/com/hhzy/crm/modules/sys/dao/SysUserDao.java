package com.hhzy.crm.modules.sys.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.sys.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:21
 * @Description:
 */
@Mapper
public interface SysUserDao extends MyMapper<SysUser> {

    @Select("select m.perms from sys_user_role ur \n" +
            " LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id \n" +
            " LEFT JOIN sys_menu m on rm.menu_id = m.menu_id \n" +
            " where ur.user_id = #{userId}")
    List<String> queryAllPerms(Long userId);


    /**
     * 查询用户的所有菜单ID
     */
    @Select("select distinct rm.menu_id from sys_user_role ur \n" +
            " LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id \n" +
            " where ur.user_id = #{userId}")
    List<Long> queryAllMenuId(Long userId);

    /**
     * 根据用户名，查询系统用户
     */
    @Select("select * from sys_user where username = #{username} or mobile=#{username}")
    SysUser queryByUserName(@Param("username") String username);

    @Select("select user_id userId,department_id departmentId from sys_user where username = #{username} or mobile=#{username}")
    Map<String,Object> queryByTest(@Param("username") String username);


    @Select("select * from sys_user where username like CONCAT('%', #{keyWord}, '%') or mobile like CONCAT('%', #{keyWord}, '%')")
    List<SysUser> selectListByKeyWord(@Param("keyWord") String keyWord);




}
