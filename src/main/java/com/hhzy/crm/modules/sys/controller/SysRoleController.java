package com.hhzy.crm.modules.sys.controller;

import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.sys.dataobject.vo.MenuCheckedVO;
import com.hhzy.crm.modules.sys.entity.SysRole;
import com.hhzy.crm.modules.sys.service.SysRoleMenuService;
import com.hhzy.crm.modules.sys.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/2 13:39
 * @Description:
 */
@RestController
@RequestMapping("/sys/role")
@Api(description = "角色列表")
public class SysRoleController  extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @GetMapping("/list")
    @ApiOperation("角色列表")
    public CommonResult list(){
        List<SysRole> list = sysRoleService.list();
        for (SysRole sysRole : list) {
            MenuCheckedVO menuCheckVo = sysRoleMenuService.getMenuCheckVo(sysRole.getRoleId());
            sysRole.setMenuCheckedVO(menuCheckVo);
            List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(sysRole.getRoleId());
            sysRole.setMenuIdList(menuIdList);
        }
        return CommonResult.success(list);
    }



    @PostMapping("/save")
    @ApiOperation("保存角色")
    public CommonResult save(@RequestBody SysRole sysRole){

        sysRole.setCreateUserId(getUserId());

        sysRoleService.save(sysRole);

        return CommonResult.success();
    }


    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId}")
    @ApiOperation("角色信息")
    public CommonResult info(@PathVariable("roleId") Long roleId){
        SysRole role = sysRoleService.selectById(roleId);
        //查询角色对应的菜单
        // List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        // role.setMenuIdList(menuIdList);
        MenuCheckedVO menuCheckVo = sysRoleMenuService.getMenuCheckVo(roleId);
        role.setMenuCheckedVO(menuCheckVo);
        return CommonResult.success();
    }



    @PostMapping("/update")
    @ApiOperation("更新角色")
    public  CommonResult update(@RequestBody SysRole sysRole){
        sysRole.setCreateUserId(getUserId());
        sysRoleService.update(sysRole);
        return CommonResult.success();

    }



    @PostMapping("/delete")
    @ApiOperation("删除角色")
    public  CommonResult delete(@RequestBody List<Long> roleIds){
        sysRoleService.deleteBatch(roleIds);
        return CommonResult.success();
    }



}
