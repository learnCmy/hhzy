package com.hhzy.crm.modules.sys.service;

import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.sys.entity.SysMenu;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:31
 * @Description:
 */
public interface SysMenuService extends BaseService<SysMenu> {

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
    List<SysMenu> queryListParentId(Long parentId, List<Long> menuIdList);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<SysMenu> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenu> queryNotButtonList();

    /**
     * 获取用户菜单列表
     */
    List<SysMenu> getUserMenuList(Long userId);

    /**
     * 获取用户菜单及权限列表
     */
     List<SysMenu> getTreePermList(List<Long> menuIdList);


    /**
     * 获取用户菜单列表
     */
    List<SysMenu> getAllMenuList(List<Long> menuIdList);

    /**
     * 删除
     */
    void delete(Long menuId);
}
