package com.hhzy.crm.modules.sys.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.sys.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:09
 * @Description:
 */
@Mapper
public interface SysMenuDao  extends MyMapper<SysMenu> {
    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    @Select("select * from sys_menu where parent_id = #{parentId} order by order_num asc")
    List<SysMenu> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    @Select("select * from sys_menu where type != 2 order by order_num asc")
    List<SysMenu> queryNotButtonList();

}
