package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.enums.SourceWayEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.EnumUtil;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.CallLogDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.CallLog;
import com.hhzy.crm.modules.customer.service.CallLogService;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.ShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * @Date: 2019/8/10 22:56
 * @Description:
 */
@RestController
@Slf4j
    @RequestMapping("/web/call")
@Api(description = "网页端来电登记管理")
public class CallLogController  extends BaseController {

    @Autowired
    private CallLogService callLogService;

    @Autowired
    private ShiroService shiroService;

    @PostMapping("list")
    @ApiOperation("来电登记列表")
    @RequiresPermissions(value = "callselect")
    public CommonResult list(@RequestBody CallLogDTO callLogDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(callLogDTO.getSortClause());
        callLogDTO.setSortClause(sortClause);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if(userPermissions.contains(CrmConstant.Permissions.MYCUSTOMER)){
            callLogDTO.setUserId(user.getUserId());
        }
        PageInfo<CallLog> callLogPageInfo = callLogService.selectList(callLogDTO);
        if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
            callLogPageInfo.getList().forEach(e->e.setMobile(null));
        }
        return CommonResult.success(callLogPageInfo);
    }


    @PostMapping("/save")
    @ApiOperation("保存来电登记")
    public CommonResult addCall(@RequestBody CallLog callLog){
        if (callLog.getProjectId()==null){
            throw  new BusinessException("房源项目Id不存在");
        }
        Long userId = getUserId();
        callLog.setUserId(userId);
        callLogService.saveCallLog(callLog);
        return CommonResult.success();
    }

    @GetMapping("/update/user")
    @ApiOperation("修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult list(Long id,Long userId){
        callLogService.updateUser(id,userId);
        return  CommonResult.success();
    }


    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult updateUSer(@RequestBody UserBatchDTO userBatchDTO){
        callLogService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新来电登记")
    @RequiresPermissions("callupdate")
    public CommonResult updateCall(@RequestBody CallLog callLog){
        if (callLog.getProjectId()==null){
            throw  new BusinessException("房源项目Id不存在");
        }
        callLogService.updateCallLog(callLog);
        return CommonResult.success();
    }

    @PostMapping("/info/{id}")
    @ApiOperation("来电登记详情")
    public CommonResult info(@PathVariable Long id){
        CallLog callLog = callLogService.selectCallLogById(id);
        return CommonResult.success(callLog);
    }


    @GetMapping("/export")
    @ApiOperation("/导出来电")
    @RequiresPermissions("export")
    @DataLog(value = "(网页)来电登记导出",actionType =CrmConstant.ActionType.EXPORT)
    public void export(CallLogDTO callLogDTO,ModelMap modelMap) throws UnsupportedEncodingException {
        String sortClause = StringHandleUtils.camel2UnderMultipleline(callLogDTO.getSortClause());
        callLogDTO.setSortClause(sortClause);
        callLogDTO.setPage(null);
        callLogDTO.setPageSize(null);
        PageInfo<CallLog> callLogPageInfo = callLogService.selectList(callLogDTO);
        List<CallLog> list = callLogPageInfo.getList();
        for (int i = 0; i <list.size() ; i++) {
            CallLog callLog = list.get(i);
            SourceWayEnum sourceWayEnum = EnumUtil.getByCode(callLog.getAcquiringWay(), SourceWayEnum.class);
            callLog.setAcquiringWayStr(sourceWayEnum==null?null:sourceWayEnum.getMessage());
            callLog.setId(Long.valueOf(i+1));
        }

        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/call.xlsx");
        map.put("list",callLogPageInfo.getList());

        modelMap.put(TemplateExcelConstants.FILE_NAME, URLEncoder.encode("来电登记表"+new DateTime().toString("yyyyMMdd"),"UTF-8"));
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }


    @PostMapping("/delete")
    @ApiOperation("/删除来电登记")
    @RequiresPermissions("delete")
    @DataLog(value = "(网页)来电登记删除",actionType =CrmConstant.ActionType.DELETE)
    public CommonResult delete(@RequestBody List<Long> callLogIdList){
        callLogService.deleteBatch(callLogIdList);
        return CommonResult.success();
    }



}
