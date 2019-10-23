package com.hhzy.crm.modules.sys.service.impl;

import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.modules.sys.dao.SysMenuDao;
import com.hhzy.crm.modules.sys.dao.SysUserDao;
import com.hhzy.crm.modules.sys.dao.SysUserTokenDao;
import com.hhzy.crm.modules.sys.entity.SysMenu;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.entity.SysUserToken;
import com.hhzy.crm.modules.sys.service.ShiroService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 17:13
 * @Description:
 */
@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserTokenDao sysUserTokenDao;


    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;
        /*if (userId==CrmConstant.SUPER_ADMIN){
            List<SysMenu> sysMenus = sysMenuDao.selectAll();
            permsList = sysMenus.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        }else {*/
            permsList=sysUserDao.queryAllPerms(userId);
       // }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserToken queryByToken(String token) {
        SysUserToken sysUserToken = sysUserTokenDao.queryByToken(token);
        return sysUserToken;
    }

    @Override
    public SysUser queryUser(Long userId) {
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
        return sysUser;
    }
}
