package com.hhzy.crm.modules.sys.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.modules.sys.entity.SysUser;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 13:32
 * @Description:
 */
public interface SysUserService {
    /**
     * 保存用户
     */
    void save(SysUser user);

    void updateStatus(Long userId,Integer status);

    /**
     * 修改用户
     */
    void update(SysUser user);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);



    /**
     * 根据用户名或者手机号，查询系统用户
     */
    SysUser queryByUserName(String username);


    public SysUser selectSysUserInfo(Long userId);


    PageInfo<SysUser> selectList(String keyWord,Integer page,Integer pageSize);


    public void deleteBatch(List<Long> userIds);

    public  void restartPassword(Long userId);

    void  updatePassword(Long userId,String passwrod);

}
