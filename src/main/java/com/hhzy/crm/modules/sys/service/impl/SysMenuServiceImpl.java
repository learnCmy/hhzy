package com.hhzy.crm.modules.sys.service.impl;

import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.modules.sys.dao.SysMenuDao;
import com.hhzy.crm.modules.sys.dao.SysRoleMenuDao;
import com.hhzy.crm.modules.sys.entity.SysMenu;
import com.hhzy.crm.modules.sys.entity.SysRoleMenu;
import com.hhzy.crm.modules.sys.service.SysMenuService;
import com.hhzy.crm.modules.sys.service.SysRoleMenuService;
import com.hhzy.crm.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:33
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements SysMenuService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Autowired
    private SysMenuDao sysMenuDao;

    @Override
    public List<SysMenu> queryListParentId(Long parentId, List<Long> menuIdList) {

        List<SysMenu> menuList = this.queryListParentId(parentId);
        if(menuIdList == null){
            return menuList;
        }

        List<SysMenu> userMenuList = new ArrayList<>();
        for(SysMenu menu : menuList){
            if(menuIdList.contains(menu.getMenuId())){
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<SysMenu> queryListParentId(Long parentId) {
        return sysMenuDao.queryListParentId(parentId);
    }


    @Override
    public List<SysMenu> queryNotButtonList() {
        return null;
    }

    @Override
    public List<SysMenu> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if(userId == CrmConstant.SUPER_ADMIN){
            return getAllMenuList(null);
        }
        //用户菜单列表
        List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }




    /**
     * 获取所有菜单列表
     */
    public List<SysMenu> getAllMenuList(List<Long> menuIdList){
        //查询根菜单列表
        List<SysMenu> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<Long> menuIdList){
        List<SysMenu> subMenuList = new ArrayList<SysMenu>();

        for(SysMenu entity : menuList){
            //目录
            if(entity.getType() == CrmConstant.MenuType.CATALOG.getValue()){
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }



    /**
     * 获取所有菜及权限列表
     */
    public List<SysMenu> getTreePermList(List<Long> menuIdList){
        //查询根菜单列表
        List<SysMenu> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getTreeList(menuList, menuIdList);

        return menuList;
    }


    /**
     * 递归
     */
    private List<SysMenu> getTreeList(List<SysMenu> menuList, List<Long> menuIdList){
        List<SysMenu> subMenuList = new ArrayList<SysMenu>();
        for(SysMenu entity : menuList){
            entity.setList(getTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            subMenuList.add(entity);
        }
        return subMenuList;
    }


    @Override
    public void delete(Long menuId) {
        //删除菜单
        int i = sysMenuDao.deleteByPrimaryKey(menuId);
        Example example = new Example(SysRoleMenu.class);
        example.createCriteria().andEqualTo("menuId",menuId);
        //删除菜单与角色关联
        sysRoleMenuDao.deleteByExample(example);
    }
}
