package com.hhzy.crm.modules.sys.service.impl;

import com.google.common.collect.Lists;
import com.hhzy.crm.modules.sys.dao.SysRoleMenuDao;
import com.hhzy.crm.modules.sys.dataobject.vo.MenuCheckedVO;
import com.hhzy.crm.modules.sys.entity.SysRoleMenu;
import com.hhzy.crm.modules.sys.entity.SysUserRole;
import com.hhzy.crm.modules.sys.service.SysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:34
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;


    @Override
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色与菜单的关系
        deleteBatch(Lists.newArrayList(roleId));
        if (menuIdList.size()==0){
            return;
        }
        //保存角色与菜单关系
        List<SysRoleMenu> list = new ArrayList<>(menuIdList.size());
        for(Long menuId : menuIdList){
            SysRoleMenu sysRoleMenuEntity = new SysRoleMenu();
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);
            list.add(sysRoleMenuEntity);
        }
        sysRoleMenuDao.insertList(list);

    }

    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return sysRoleMenuDao.queryMenuIdList(roleId);
    }


    @Override
    public MenuCheckedVO getMenuCheckVo(Long roleId) {
        MenuCheckedVO menuCheckedVO = new MenuCheckedVO();
        List<Long> checked = sysRoleMenuDao.queryMenuIdListByCheck(roleId, true);
        List<Long> halfChecked = sysRoleMenuDao.queryMenuIdListByCheck(roleId, false);
        menuCheckedVO.setChecked(checked);
        menuCheckedVO.setHalfCheckedKeys(halfChecked);
        return menuCheckedVO;
    }


    @Override
    public int deleteBatch(List<Long> roleIds) {
        Example example = new Example(SysUserRole.class);
        example.createCriteria().andIn("roleId",roleIds);
        int i = sysRoleMenuDao.deleteByExample(example);
        return i;
    }

    @Override
    public void saveOrUpdate(Long roleId, MenuCheckedVO menuCheckedVO) {
        //先删除角色与菜单的关系
        deleteBatch(Lists.newArrayList(roleId));
        List<Long> halfChecked = menuCheckedVO.getHalfCheckedKeys();
        List<Long> checked = menuCheckedVO.getChecked();
        if (CollectionUtils.isNotEmpty(halfChecked)){
            insertList(roleId,halfChecked,false);
        }
        if (CollectionUtils.isNotEmpty(checked)){
            insertList(roleId,checked,true);
        }
    }



    public void insertList(Long roleId,List<Long> menuIdList,Boolean checkAll){
        List<SysRoleMenu> list = new ArrayList<>(menuIdList.size());
        for(Long menuId : menuIdList){
            SysRoleMenu sysRoleMenuEntity = new SysRoleMenu();
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);
            sysRoleMenuEntity.setAllChecked(checkAll);
            list.add(sysRoleMenuEntity);
        }
        sysRoleMenuDao.insertList(list);
    }
}
