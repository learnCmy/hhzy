package com.hhzy.crm.modules.sys.controller;

import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.sys.entity.SysDepartment;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.ShiroService;
import com.hhzy.crm.modules.sys.service.SysDepartmentService;
import com.hhzy.crm.modules.sys.service.SysUserService;
import com.hhzy.crm.modules.sys.service.SysUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 21:23
 * @Description:
 */
@RestController
@Api(description = "用户登录接口")
public class SysLoginController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @PostMapping("/sys/login")
    @ApiOperation(value = "用户登录")
    public CommonResult login(String name,String password,Integer loginClient){

        SysUser sysUser = sysUserService.queryByUserName(name);

        if (sysUser==null){
            return CommonResult.build(500,"您的账号不存在，请联系管理员创建");
        }

        if(!sysUser.getLoginClient().equals(3)&&!loginClient.equals(sysUser.getLoginClient())) {
            return CommonResult.build(500, "您无权限登录");
        }

        if (sysUser==null||!sysUser.getPassword().equals(new Sha256Hash(password, sysUser.getSalt()).toHex())){
            return CommonResult.build(500,"账号或密码错误");
        }

        //账号锁定
        if(sysUser.getStatus()==0){
            return CommonResult.build(500,"账号已被锁定,请联系管理员");
        }
        CommonResult result = sysUserTokenService.createToken(sysUser.getUserId());
        return result;
    }


    @PostMapping("/sys/userInfo")
    @ApiOperation("角色信息")
    public CommonResult info(){
        SysUser sysUser=(SysUser)SecurityUtils.getSubject().getPrincipal();
        Set<String> userPermissions = shiroService.getUserPermissions(sysUser.getUserId());
        sysUser.setPermissionsSet(userPermissions);
        SysDepartment dept = sysDepartmentService.queryById(sysUser.getDepartmentId());
        if (dept!=null){
            sysUser.setDeptName(dept.getName());
        }
        return CommonResult.success(sysUser);
    }


    /**
     * 退出
     */
    @PostMapping("/sys/logout")
    @ApiOperation("退出")
    public  CommonResult logout(){

        SysUser sysUser=(SysUser)SecurityUtils.getSubject().getPrincipal();

        sysUserTokenService.logout(sysUser.getUserId());

        return CommonResult.success();
    }




}
