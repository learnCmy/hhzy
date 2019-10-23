package com.hhzy.crm.modules.sys.service.impl;

import com.hhzy.crm.modules.sys.dao.SysRoleDao;
import com.hhzy.crm.modules.sys.dataobject.vo.MenuCheckedVO;
import com.hhzy.crm.modules.sys.entity.SysRole;
import com.hhzy.crm.modules.sys.entity.SysUserRole;
import com.hhzy.crm.modules.sys.service.SysRoleMenuService;
import com.hhzy.crm.modules.sys.service.SysRoleService;
import com.hhzy.crm.modules.sys.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.management.relation.Role;
import java.util.Date;
import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 15:57
 * @Description:
 */
@Transactional
@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysRoleMenuService  sysRoleMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;


    @Override
    public List<SysRole> list() {
        List<SysRole> sysRoles = sysRoleDao.selectAll();
        return sysRoles;
    }

    @Override
    public SysRole selectById(Long roleId) {
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(roleId);
        return sysRole;
    }

    @Override
    public void save(SysRole sysRole) {
        sysRole.setCreateTime(new Date());

        sysRoleDao.insert(sysRole);

        //保存角色与菜单关系
       /* if (CollectionUtils.isNotEmpty(sysRole.getMenuIdList())){
            sysRoleMenuService.saveOrUpdate(sysRole.getRoleId(),sysRole.getMenuIdList());
        }*/
        //保存角色与菜单关系
       if (sysRole.getMenuCheckedVO()!=null){
           sysRoleMenuService.saveOrUpdate(sysRole.getRoleId(),sysRole.getMenuCheckedVO());
       }


    }

    @Override
    public void update(SysRole sysRole) {

        sysRoleDao.updateByPrimaryKey(sysRole);
        /*//保存角色与菜单关系
        if (CollectionUtils.isNotEmpty(sysRole.getMenuIdList())){
            sysRoleMenuService.saveOrUpdate(sysRole.getRoleId(),sysRole.getMenuCheckedVO());
        }
*/
        //保存角色与菜单关系
        if (sysRole.getMenuCheckedVO()!=null){
            sysRoleMenuService.saveOrUpdate(sysRole.getRoleId(),sysRole.getMenuCheckedVO());
        }

    }

    @Override
    public void deleteBatch(List<Long> roleIds) {
        //删除角色
        Example example = new Example(SysRole.class);
        example.createCriteria().andIn("roleId",roleIds);
        sysRoleDao.deleteByExample(example);

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);


    }
}
