package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.TookeenDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.entity.Tookeen;
import com.hhzy.crm.modules.customer.service.ProjectService;
import com.hhzy.crm.modules.customer.service.TookeenService;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.ShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private ShiroService shiroService;

    @PostMapping("list")
    @ApiOperation("拓展客户列表")
    @RequiresPermissions("tookeenselect")
    public CommonResult list(@RequestBody TookeenDTO tookeenDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(tookeenDTO.getSortClause());
        tookeenDTO.setSortClause(sortClause);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if(userPermissions.contains(CrmConstant.Permissions.MYCUSTOMER)){
            tookeenDTO.setUserId(user.getUserId());
        }
        PageInfo<Tookeen> tookeenPageInfo = tookeenService.selectList(tookeenDTO);
        return CommonResult.success(tookeenPageInfo);
    }

    @PostMapping("/save")
    @ApiOperation("录入客户")
    public CommonResult saveTookeen(@RequestBody Tookeen tookeen){
        Long userId = getUserId();
        tookeen.setUserId(userId);
        tookeenService.saveBasicTookeen(tookeen);
        return CommonResult.success();
    }


    @PostMapping("/update/user")
    @ApiOperation("修改置业顾问")
    @RequiresPermissions("updateuser")
    @DataLog(value = "拓展修改置业顾问",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateUser(Long id, Long userId){
        tookeenService.updateUser(id,userId);
        return  CommonResult.success();

    }

    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    @DataLog(value = "拓展批量修改置业顾问",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateUSerBatch(@RequestBody UserBatchDTO userBatchDTO){
        tookeenService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }



    @PostMapping("/delete")
    @ApiOperation("删除拓展信息")
    @RequiresPermissions("delete")
    @DataLog(value = "删除拓展信息",actionType =CrmConstant.ActionType.DELETE)
    public CommonResult delete(@RequestBody List<Long> ids){
        tookeenService.deleteBatch(ids);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新拓展客户信息")
    @RequiresPermissions("tookeenupdate")
    @DataLog(value = "更新拓展客户信息",actionType =CrmConstant.ActionType.UPDATE)
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
    @ApiOperation("/拓展客户表导出")
    @DataLog(value = "拓展客户表导出",actionType =CrmConstant.ActionType.EXPORT)
    public void export(TookeenDTO tookeenDTO,ModelMap modelMap) throws UnsupportedEncodingException {
        String sortClause = StringHandleUtils.camel2UnderMultipleline(tookeenDTO.getSortClause());
        tookeenDTO.setSortClause(sortClause);
        tookeenDTO.setPage(null);
        tookeenDTO.setPageSize(null);
        PageInfo<Tookeen> tookeenPageInfo = tookeenService.selectList(tookeenDTO);
        List<Tookeen> list = tookeenPageInfo.getList();
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/tookeen.xlsx");
        map.put("list",list);
        Project project = projectService.queryById(tookeenDTO.getProjectId());
        map.put("projectName",project.getProjectName());
        modelMap.put(TemplateExcelConstants.FILE_NAME, URLEncoder.encode("拓展客户登记表"+new DateTime().toString("yyyyMMdd"),"UTF-8"));
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

}
