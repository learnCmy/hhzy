package com.hhzy.crm.modules.sys.service.impl;

import com.hhzy.crm.modules.sys.dao.SysUserRoleDao;
import com.hhzy.crm.modules.sys.entity.SysRole;
import com.hhzy.crm.modules.sys.entity.SysUserRole;
import com.hhzy.crm.modules.sys.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 15:58
 * @Description:
 */
@Service
@Slf4j
@Transactional
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        Example example = new Example(SysUserRole.class);
        example.createCriteria().andEqualTo("userId",userId);
        sysUserRoleDao.deleteByExample(example);

        //保存用户与角色关系
        List<SysUserRole> list = new ArrayList<>(roleIdList.size());
        for(Long roleId : roleIdList){
            SysUserRole sysUserRoleEntity = new SysUserRole();
            sysUserRoleEntity.setUserId(userId);
            sysUserRoleEntity.setRoleId(roleId);
            list.add(sysUserRoleEntity);
        }
        sysUserRoleDao.insertList(list);
    }

    @Override
    public List<SysRole> queryRoleList(Long userId) {

        List<SysRole> sysRoleList = sysUserRoleDao.queryRoleList(userId);

        return sysRoleList;
    }

    @Override
    public int deleteBatch(List<Long> roleIds) {
        Example example = new Example(SysUserRole.class);
        example.createCriteria().andIn("roleId",roleIds);
        int i = sysUserRoleDao.deleteByExample(example);
        return i;
    }
}
