package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.FollowLogDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.FollowLog;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.service.FollowLogService;
import com.hhzy.crm.modules.customer.service.ProjectService;
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
 * @Date: 2019/8/28 16:40
 * @Description:
 */
@RestController
@Api(description = "网页端跟进记录管理")
@RequestMapping("/web/follow")
public class FollowLogController extends BaseController {

    @Autowired
    private FollowLogService followLogService;


    @Autowired
    private ShiroService shiroService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/list")
    @ApiOperation("跟进记录列表")
    @RequiresPermissions("followselect")
    public CommonResult list(@RequestBody FollowLogDTO followLogDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(followLogDTO.getSortClause());
        followLogDTO.setSortClause(sortClause);
        PageInfo<FollowLog> select = followLogService.select(followLogDTO);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
            select.getList().forEach(e->e.setMobile(null));
        }
        return CommonResult.success(select);
    }


    @GetMapping("/update/user")
    @ApiOperation("修改置业顾问")
    @RequiresPermissions("updateuser")
    @DataLog(value = "跟进记录置业顾问修改",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateUser(Long id,Long userId){
        followLogService.updateUser(id,userId);
        return  CommonResult.success();
    }


    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    @DataLog(value = "跟进记录置业顾问批量修改",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateUserBatch(@RequestBody UserBatchDTO userBatchDTO){
        followLogService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }



    @PostMapping("/update")
    @ApiOperation("更新跟进记录")
    @RequiresPermissions("followupdate")
    @DataLog(value = "(网页)跟进记录修改",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult update(@RequestBody FollowLog followLog){
        if (followLog.getProjectId()==null){
            throw  new BusinessException("项目id不能为空");
        }
        followLogService.update(followLog);
        return CommonResult.success();
    }

    @PostMapping("/info/{id}")
    @ApiOperation("跟进记录详情")
    public CommonResult info(@PathVariable Long id){
        FollowLog followLog = followLogService.selectById(id);
        return CommonResult.success(followLog);
    }



    @PostMapping("/delete")
    @ApiOperation("删除跟进信息")
    @RequiresPermissions("delete")
    @DataLog(value = "跟进记录删除",actionType =CrmConstant.ActionType.DELETE)
    public CommonResult delete(@RequestBody List<Long> followIdLogList){
        followLogService.deleteBatch(followIdLogList);
        return CommonResult.success();
    }


    @GetMapping("/export")
    @ApiOperation("跟进导出")
    @RequiresPermissions("export")
    @DataLog(value = "跟进记录导出",actionType =CrmConstant.ActionType.EXPORT)
    public void export( FollowLogDTO followLogDTO,ModelMap modelMap ) throws UnsupportedEncodingException {
        String sortClause = StringHandleUtils.camel2UnderMultipleline(followLogDTO.getSortClause());
        followLogDTO.setSortClause(sortClause);
        followLogDTO.setPage(null);
        followLogDTO.setPageSize(null);
        PageInfo<FollowLog> select = followLogService.select(followLogDTO);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        List<FollowLog> list = select.getList();
        for (FollowLog followLog : list) {
            if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
                followLog.setMobile(null);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/follow.xlsx");
        map.put("list",list);
        Project project = projectService.queryById(followLogDTO.getProjectId());
        map.put("projectName",project.getProjectName());
        modelMap.put(TemplateExcelConstants.FILE_NAME, URLEncoder.encode("跟进记录表"+new DateTime().toString("yyyyMMdd"),"UTF-8"));
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }




}
