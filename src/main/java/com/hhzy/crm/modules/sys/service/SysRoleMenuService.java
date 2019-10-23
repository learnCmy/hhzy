package com.hhzy.crm.modules.sys.service;

import com.hhzy.crm.modules.sys.dataobject.vo.MenuCheckedVO;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:32
 * @Description:
 */
public interface SysRoleMenuService {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(List<Long> roleIds);

    void saveOrUpdate(Long roleId, MenuCheckedVO menuCheckedVO);

    MenuCheckedVO getMenuCheckVo(Long roleId);
}
