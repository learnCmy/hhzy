package com.hhzy.crm.modules.customer.controller.web;

import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/8/29 11:22
 * @Description:
 */
@RestController
@Api(description = "项目管理基础配置")
@RequestMapping("/web/project")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;


    @PostMapping("/save")
    @ApiOperation("项目管理基本配置新增")
    public CommonResult save(@RequestBody Project project){
        projectService.save(project);
        return  CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("项目管理基本配置更新")
    public CommonResult update(@RequestBody  Project project){
        projectService.updateSelective(project);
        return CommonResult.success();
    }


    @PostMapping("/select")
    @ApiOperation("所有项目查询列表")
    public CommonResult select(){
        List<Project> projects = projectService.queryAll();
        return CommonResult.success(projects);
    }

    @GetMapping("/config/{projectId}")
    @ApiOperation("查询项目基础信息查询")
    public CommonResult selectConfig(@PathVariable Long projectId){

        Map<String, Object> map = projectService.selectConfig(projectId);

        return  CommonResult.success(map);
    }



    @PostMapping("/delete")
    @ApiOperation("删除项目")
    @RequiresPermissions("delete")
    public CommonResult delete(@RequestBody List<Long> projectIdList){
        projectService.deleteBatch(projectIdList);
        return CommonResult.success();
    }


}
