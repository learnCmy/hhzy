package com.hhzy.crm.modules.sys.controller;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/1 11:30
 * @Description:
 */
@RestController
@RequestMapping("/sys/user")
@Api(description = "员工管理")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/save")
    @ApiOperation("保存员工")
    public CommonResult addSysUser(@RequestBody SysUser sysUser){
        sysUserService.save(sysUser);
        return CommonResult.success();
    }


    @GetMapping("/list")
    @ApiOperation("员工列表")
    public CommonResult selectAll(String keyWord,Integer page,Integer pageSize){
        PageInfo<SysUser> sysUserPageInfo = sysUserService.selectList(keyWord, page, pageSize);
        return CommonResult.success(sysUserPageInfo);
    }


    @PostMapping("/update")
    @ApiOperation("更新员工")
    public CommonResult updateSysUser(@RequestBody SysUser sysUser){
        sysUser.setCreateUserId(getUserId());
        sysUserService.update(sysUser);
        return  CommonResult.success();
    }

    @PostMapping("/info/{userId}")
    @ApiOperation("员工信息回显")
    public CommonResult sysUserInfo(@PathVariable Long userId){
        SysUser sysUser = sysUserService.selectSysUserInfo(userId);
        return CommonResult.success(sysUser);
    }


    @PostMapping("/delete")
    @ApiOperation("删除用户")
    public CommonResult deleteSysUser(@RequestBody List<Long> userIds){
        sysUserService.deleteBatch(userIds);
        return CommonResult.success();
    }

    @GetMapping("/ban")
    @ApiOperation("禁用用户")
    public CommonResult ban(Long userId,Integer status){
        sysUserService.updateStatus(userId,status);
        return CommonResult.success();
    }


    @PostMapping("/restart/{userId}")
    @ApiOperation("重置密码")
    public  CommonResult restart(@PathVariable Long userId){
       sysUserService.restartPassword(userId);
        return  CommonResult.success();
    }

    @PostMapping("update/password")
    @ApiOperation("修改密码")
    public CommonResult updatePassword(Long userId,String password){
        sysUserService.updatePassword(userId,password);
        return  CommonResult.success();
    }


}
