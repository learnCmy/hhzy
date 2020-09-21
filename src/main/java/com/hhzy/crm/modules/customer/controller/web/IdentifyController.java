package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.enums.IdentifySellStatusEnum;
import com.hhzy.crm.common.enums.SourceWayEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.EnumUtil;
import com.hhzy.crm.common.utils.MobileUtils;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.IdentifyDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.RefundDateDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.IdentifyImport;
import com.hhzy.crm.modules.customer.entity.IdentifyLog;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.service.IdentifyService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: cmy
 * @Date: 2019/8/28 16:44
 * @Description:
 */
@RestController
@Api(description = "网页端认筹管理")
@RequestMapping("/web/identity")
public class IdentifyController extends BaseController {

    @Autowired
    private IdentifyService identifyService;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/list")
    @ApiOperation("认筹记录列表")
    @RequiresPermissions("identifyselect")
    public CommonResult list(@RequestBody IdentifyDTO identifyDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(identifyDTO.getSortClause());
        identifyDTO.setSortClause(sortClause);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if(userPermissions.contains(CrmConstant.Permissions.MYCUSTOMER)){
            identifyDTO.setUserId(user.getUserId());
        }
        PageInfo<IdentifyLog> identifyLogPageInfo = identifyService.selectList(identifyDTO);
        if (userPermissions.contains(CrmConstant.Permissions.SHOP)&&!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)){
            identifyDTO.setProductType(2);
        }else if (userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            identifyDTO.setProductType(1);
        }else if (!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            return CommonResult.success(new PageInfo<IdentifyLog>());
        }
        if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
            identifyLogPageInfo.getList().forEach(e->e.setMobile(null));
        }
        return CommonResult.success(identifyLogPageInfo);
    }


    @PostMapping("/save")
    @ApiOperation("保存认筹记录")
    public CommonResult save(@RequestBody IdentifyLog identifyLog){
        if (identifyLog.getProjectId()==null){
            throw  new BusinessException("项目Id不能为空");
        }
        Long userId = getUserId();
        String mobile = MobileUtils.getMobile(identifyLog.getMobile());
        identifyLog.setMobile(mobile);
        identifyLog.setUserId(userId);
        identifyLog.setSellStatus(IdentifySellStatusEnum.BUYCARD.getCode());
        identifyService.save(identifyLog);
        return CommonResult.success();
    }

    @GetMapping("/count/status")
    @ApiOperation("统计不同状态下的认筹总数")
    public CommonResult countStatus(Long projectId){
        Map<String, Object> map = identifyService.countNumberByStatus(projectId);
        return CommonResult.success(map);
    }


    @GetMapping("/update/user")
    @ApiOperation("修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult list(Long id,Long userId){
        identifyService.updateUser(id,userId);
        return  CommonResult.success();
    }


    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult updateUSer(@RequestBody UserBatchDTO userBatchDTO){
        identifyService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新认筹记录")
    @RequiresPermissions("identifyupdate")
    @DataLog(value = "更新认筹记录",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult update(@RequestBody IdentifyLog identifyLog){
        if (identifyLog.getProjectId()==null){
            throw  new BusinessException("项目Id不能为空");
        }
        identifyService.updateByPrimaryKeySelective(identifyLog);
        return CommonResult.success();
    }

    @PostMapping("/update/refundDate")
    @ApiOperation("更新退卡日期")
    @DataLog(value = "认筹更新退卡日期",actionType =CrmConstant.ActionType.UPDATE)
    public  CommonResult updateRefundDate(@RequestBody RefundDateDTO refundDateDTO){
        IdentifyLog identifyLog = new IdentifyLog();
        identifyLog.setProjectId(refundDateDTO.getProjectId());
        identifyLog.setId(refundDateDTO.getId());
        identifyLog.setRefundTime(refundDateDTO.getRefundDate());
        identifyService.updateByPrimaryKeySelective(identifyLog);
        return CommonResult.success();
    }


    @PostMapping("/info/{id}")
    @ApiOperation("认筹记录详情")
    @RequiresPermissions("identify")
    public CommonResult info(@PathVariable Long id){
        IdentifyLog identifyLog = identifyService.selectById(id);
        return CommonResult.success(identifyLog);
    }



    @GetMapping("/export")
    @ApiOperation("认筹导出")
    @RequiresPermissions("export")
    @DataLog(value = "认筹数据导出",actionType =CrmConstant.ActionType.EXPORT)
    public void export(ModelMap modelMap,IdentifyDTO identifyDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(identifyDTO.getSortClause());
        identifyDTO.setSortClause(sortClause);
        identifyDTO.setPage(null);
        identifyDTO.setPageSize(null);
        PageInfo<IdentifyLog> identifyLogPageInfo = identifyService.selectList(identifyDTO);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        List<IdentifyLog> list = identifyLogPageInfo.getList();
        for (int i = 0; i <list.size() ; i++) {
            IdentifyLog identifyLog = list.get(i);
            identifyLog.setId(Long.valueOf(i));
            SourceWayEnum sourceWayEnum = EnumUtil.getByCode(identifyLog.getSourceWay(), SourceWayEnum.class);
            identifyLog.setSourceWayStr(sourceWayEnum==null?null:sourceWayEnum.getMessage());
            if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
                identifyLog.setMobile(null);
            }
            IdentifySellStatusEnum identifySellStatusEnum = EnumUtil.getByCode(identifyLog.getSellStatus(), IdentifySellStatusEnum.class);
            identifyLog.setSellStatusStr(identifySellStatusEnum==null?null:identifySellStatusEnum.getMessage());
            if (Integer.valueOf(1).equals(identifyLog.getProductType())){
                identifyLog.setProductTypeStr("住宅");
            }else if (Integer.valueOf(2).equals(identifyLog.getProductType())){
                identifyLog.setProductTypeStr("商铺");
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/identify.xlsx");
        map.put("list",list);
        Project project = projectService.queryById(identifyDTO.getProjectId());
        map.put("projectName",project.getProjectName());
        String yyyyMMdd = new DateTime().toString("yyyyMMdd");
        String fileName="";
        if (identifyDTO.getProductType()!=null){
            if (Integer.valueOf(1).equals(identifyDTO.getProductType())){
                fileName= "住宅"+"认筹登记表"+yyyyMMdd;
            }else if (Integer.valueOf(2).equals(identifyDTO.getProductType())){
                fileName="商铺"+"认筹登记表"+yyyyMMdd;
            }
        }else {
            fileName="认筹登记表"+yyyyMMdd;
        }
        try {
            modelMap.put(TemplateExcelConstants.FILE_NAME, URLEncoder.encode(fileName,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            modelMap.put(TemplateExcelConstants.FILE_NAME,fileName);
        }
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    @PostMapping("/delete")
    @ApiOperation("删除认筹信息")
    @RequiresPermissions("delete")
    @DataLog(value = "认筹数据删除",actionType =CrmConstant.ActionType.DELETE)
    public CommonResult delete(@RequestBody List<Long> identityLogIdList){
        identifyService.deleteBatch(identityLogIdList);
        return CommonResult.success();
    }


    @PostMapping("/updateSellstatus")
    @ApiOperation("修改认筹买售状态")
    @RequiresPermissions("identifysellstatus")
    @DataLog(value = "修改认筹售卖状态",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateSellStatus(Long id,Integer sellStatus){
        identifyService.updateSellStatus(id,sellStatus);
        return  CommonResult.success();
    }


    @PostMapping(value = "/import/{projectId}")
    @ApiOperation("导入数据")
    @RequiresPermissions("export")
    @DataLog(value = "认筹数据导入",actionType =CrmConstant.ActionType.IMPORT)
    public CommonResult excelImport(@PathVariable(value = "projectId") Long projectId, @RequestParam("file")MultipartFile file){
        ImportParams importParams = new ImportParams();
        importParams.setTitleRows(1);
        List<IdentifyImport> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), IdentifyImport.class, importParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = identifyService.importDatas(list, projectId);
        return CommonResult.success(map);
    }




}
