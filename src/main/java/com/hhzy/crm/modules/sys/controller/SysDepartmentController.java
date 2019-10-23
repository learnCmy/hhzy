package com.hhzy.crm.modules.sys.controller;

import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.sys.entity.SysDepartment;
import com.hhzy.crm.modules.sys.service.SysDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/1 11:43
 * @Description:
 */
@RestController
@RequestMapping("/dept")
@Api(description = "部门管理")
public class SysDepartmentController {

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @GetMapping("/list")
    @ApiOperation("查询子部门")
    public CommonResult ListDepartment(Long parentId){
        if (parentId==null){
            parentId=0l;
        }
        List<SysDepartment> sysDepartments = sysDepartmentService.queryDepartment(parentId);

        return CommonResult.success(sysDepartments);
    }

    @PostMapping("/list/all")
    @ApiOperation("所有部门列表")
    public  CommonResult listAll(){
        List<SysDepartment> sysDepartments = sysDepartmentService.listAll();
        return CommonResult.success(sysDepartments);
    }




    @PostMapping("/save")
    @ApiOperation("新增部门")
    public CommonResult saveDepartment(@RequestBody SysDepartment sysDepartment){
        if (sysDepartment.getParentId()==null){
            sysDepartment.setParentId(0l);
        }
        sysDepartmentService.save(sysDepartment);

        return CommonResult.success();
    }

    @PostMapping("/update")
    @ApiOperation("更新部门")
    public CommonResult updateDepartment(@RequestBody SysDepartment sysDepartment){
        sysDepartmentService.updateSelective(sysDepartment);
        return CommonResult.success();
    }


    @PostMapping("/delete")
    @ApiOperation("删除部门")
    public CommonResult deleteDepartment(@RequestBody List<Long> ids){
        sysDepartmentService.deleteByIds(ids);
        return CommonResult.success();
    }

}
