package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.TookeenDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.entity.Tookeen;
import com.hhzy.crm.modules.customer.service.ProjectService;
import com.hhzy.crm.modules.customer.service.TookeenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/10/23 15:14
 * @Description:
 */
@RestController
@Api(description = "网页端拓展客户管理")
@RequestMapping("/web/tookeen")
public class TookeenController extends BaseController {

    @Autowired
    private TookeenService tookeenService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("list")
    @ApiOperation("拓展客户列表")
    public CommonResult list(@RequestBody TookeenDTO tookeenDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(tookeenDTO.getSortClause());
        tookeenDTO.setSortClause(sortClause);
        PageInfo<Tookeen> tookeenPageInfo = tookeenService.selectList(tookeenDTO);
        return CommonResult.success(tookeenPageInfo);
    }



    @PostMapping("/update/user")
    @ApiOperation("修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult updateUser(Long id, Long userId){
        tookeenService.updateUser(id,userId);
        return  CommonResult.success();

    }

    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult updateUSer(@RequestBody UserBatchDTO userBatchDTO){
        tookeenService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }



    @PostMapping("/delete")
    @ApiOperation("删除认筹信息")
    public CommonResult delete(@RequestBody List<Long> ids){
        tookeenService.deleteBatch(ids);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新拓展客户信息")
    public CommonResult updateTookeen(@RequestBody Tookeen tookeen){
        tookeenService.updateBasicTookeen(tookeen);
        return CommonResult.success();
    }

    @PostMapping("/info/{id}")
    @ApiOperation("拓展客户详细信息")
    public CommonResult info(@PathVariable Long id){
        Tookeen tookeen = tookeenService.queryById(id);
        return  CommonResult.success(tookeen);
    }


    @GetMapping("/export")
    @ApiOperation("/来访客户登记导出")
    public void export(ModelMap modelMap, TookeenDTO tookeenDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(tookeenDTO.getSortClause());
        tookeenDTO.setSortClause(sortClause);
        PageInfo<Tookeen> tookeenPageInfo = tookeenService.selectList(tookeenDTO);
        List<Tookeen> list = tookeenPageInfo.getList();
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/tookeen.xlsx");
        map.put("list",list);
        Project project = projectService.queryById(tookeenDTO.getProjectId());
        map.put("projectName",project.getProjectName());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "拓展客户登记表");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

}
