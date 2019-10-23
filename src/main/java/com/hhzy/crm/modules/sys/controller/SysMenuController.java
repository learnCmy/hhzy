package com.hhzy.crm.modules.sys.controller;

import com.google.common.collect.Maps;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.sys.entity.SysMenu;
import com.hhzy.crm.modules.sys.service.ShiroService;
import com.hhzy.crm.modules.sys.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: cmy
 * @Date: 2019/8/2 16:51
 * @Description:
 */
@RestController
@Api(description = "菜单管理")
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private ShiroService shiroService;

    /**
     * 所有菜单列表
     */
    @GetMapping("/list")
    @ApiOperation("获取所有菜单和权限列表")
    public CommonResult list(){
        List<SysMenu> treePermList = sysMenuService.getTreePermList(null);
        List<SysMenu> webMenuList = treePermList.stream().filter(sysMenu ->
                sysMenu.getClient().equals(1)).collect(Collectors.toList());
        List<SysMenu> appMenuList = treePermList.stream().filter(sysMenu ->
                sysMenu.getClient().equals(2)).collect(Collectors.toList());
        Map<Object, Object> map = Maps.newHashMap();
        map.put("webMenuList",webMenuList);
        map.put("appMenuList",appMenuList);
        return CommonResult.success(map);
    }


    /**
     * 导航菜单
     */
    @GetMapping("/nav")
    @ApiOperation("导航菜单")
    public CommonResult nav(){
        List<SysMenu> menuList = sysMenuService.getUserMenuList(getUserId());

        Set<String> permissions = shiroService.getUserPermissions(getUserId());

        HashMap<String, Object> map = Maps.newHashMap();

        List<SysMenu> webMenuList = menuList.stream().filter(sysMenu ->
                sysMenu.getClient().equals(1)).collect(Collectors.toList());
        List<SysMenu> appMenuList = menuList.stream().filter(sysMenu ->
                sysMenu.getClient().equals(2)).collect(Collectors.toList());

        map.put("webMenuList",webMenuList);

        map.put("appMenuList",appMenuList);

        map.put("permissions",permissions);

       return CommonResult.success(map);
    }

}
