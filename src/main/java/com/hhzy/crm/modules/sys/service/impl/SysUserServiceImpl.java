package com.hhzy.crm.modules.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.modules.sys.dao.SysRoleDao;
import com.hhzy.crm.modules.sys.dao.SysUserDao;
import com.hhzy.crm.modules.sys.dao.SysUserRoleDao;
import com.hhzy.crm.modules.sys.entity.SysDepartment;
import com.hhzy.crm.modules.sys.entity.SysRole;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.entity.SysUserRole;
import com.hhzy.crm.modules.sys.service.SysDepartmentService;
import com.hhzy.crm.modules.sys.service.SysUserRoleService;
import com.hhzy.crm.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 15:59
 * @Description:
 */
@Transactional
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Override
    public void save(SysUser user) {
        SysUser sysUser = sysUserDao.queryByUserName(user.getUsername());
        SysUser sysUser2 = sysUserDao.queryByUserName(user.getMobile());
        if (sysUser!=null||sysUser2!=null){
            throw new BusinessException("用户名或手机号已存在");
        }

        if (StringUtils.isEmpty(user.getPassword())){
            user.setPassword("123456");
        }
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);
        user.setCreateTime(new Date());
        sysUserDao.insertSelective(user);
        List<Long> roleIdList = user.getRoleIdList();
        //保存用户与角色关系
        if (CollectionUtils.isNotEmpty(roleIdList)){
            sysUserRoleService.saveOrUpdate(user.getUserId(), roleIdList);
        }

    }

    @Override
    public void updateStatus(Long userId, Integer status) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setStatus(status);
        sysUserDao.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public void update(SysUser user) {
        SysUser sysUser = sysUserDao.queryByUserName(user.getUsername());
        SysUser sysUser2 = sysUserDao.queryByUserName(user.getMobile());
        if (sysUser!=null&&!sysUser.getUserId().equals(user.getUserId())){
            throw new BusinessException("用户名或手机号已存在");
        }
        if (sysUser2!=null&&!sysUser2.getUserId().equals(user.getUserId())){
            throw new BusinessException("用户名或手机号已存在");
        }
        if (StringUtils.isNotEmpty(user.getPassword())){
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
            user.setSalt(salt);
        }
        sysUserDao.updateByPrimaryKeySelective(user);
        List<Long> roleIdList = user.getRoleIdList();
        //保存用户与角色关系
        if (CollectionUtils.isNotEmpty(roleIdList)){
            sysUserRoleService.saveOrUpdate(user.getUserId(), roleIdList);
        }

    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return sysUserDao.queryAllMenuId(userId);
    }

    @Override
    public SysUser queryByUserName(String username) {
        SysUser sysUser = sysUserDao.queryByUserName(username);
        return sysUser;
    }

    @Override
    public PageInfo<SysUser> selectList(String keyWord, Integer page, Integer pageSize) {
        if (page!=null&&pageSize!=null){
            PageHelper.startPage(page,pageSize);
        }
        if (keyWord==null){
            keyWord="";
        }
        List<SysUser> sysUsers = sysUserDao.selectListByKeyWord(keyWord);
        sysUsers.forEach(sysUser->{
            List<SysRole> roleList= sysUserRoleService.queryRoleList(sysUser.getUserId());
            sysUser.setRoleList(roleList);
            SysDepartment sysDepartment = sysDepartmentService.queryById(sysUser.getDepartmentId());
            if (sysDepartment!=null){
                sysUser.setDeptName(sysDepartment.getName());
            }
        });
        return new PageInfo<>(sysUsers);
    }

    @Override
    public void deleteBatch(List<Long> userIds) {
        Example example = new Example(SysUser.class);
        example.createCriteria().andIn("userId",userIds);
        sysUserDao.deleteByExample(example);
    }

    public SysUser selectSysUserInfo(Long userId){
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);

        List<SysRole> roleList= sysUserRoleService.queryRoleList(userId);

        sysUser.setRoleList(roleList);

        return sysUser;
    }


    public  void restartPassword(Long userId){
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
        if (sysUser==null){
            throw  new BusinessException("此用户已经不存在，请刷新页面");
        }
        SysUser user = new SysUser();
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash("123456", salt).toHex());
        user.setSalt(salt);
        user.setUserId(userId);
        sysUserDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public void updatePassword(Long userId, String passwrod) {
        SysUser user = new SysUser();
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(passwrod, salt).toHex());
        user.setSalt(salt);
        user.setUserId(userId);
        sysUserDao.updateByPrimaryKeySelective(user);

    }

    @Override
    public String getUserName(Long userId) {
        String username="";
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
        if (sysUser!=null){
            username = sysUser.getUsername();
        }
        return username;
    }


}
