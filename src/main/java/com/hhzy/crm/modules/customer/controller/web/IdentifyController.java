package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.enums.IdentifySellStatusEnum;
import com.hhzy.crm.common.enums.SourceWayEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.EnumUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
    @RequiresPermissions("identify")
    public CommonResult list(@RequestBody IdentifyDTO identifyDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(identifyDTO.getSortClause());
        identifyDTO.setSortClause(sortClause);
        PageInfo<IdentifyLog> identifyLogPageInfo = identifyService.selectList(identifyDTO);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
            identifyLogPageInfo.getList().forEach(e->e.setMobile(null));
        }
        return CommonResult.success(identifyLogPageInfo);
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
    @RequiresPermissions("identify")
    public CommonResult update(@RequestBody IdentifyLog identifyLog){
        if (identifyLog.getProjectId()==null){
            throw  new BusinessException("项目Id不能为空");
        }
        identifyService.updateByPrimaryKeySelective(identifyLog);
        return CommonResult.success();
    }

    @PostMapping("/update/refundDate")
    @ApiOperation("更新退卡日期")
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
    public void export(ModelMap modelMap,IdentifyDTO identifyDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(identifyDTO.getSortClause());
        identifyDTO.setSortClause(sortClause);
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
        }
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/identify.xlsx");
        map.put("list",list);
        Project project = projectService.queryById(identifyDTO.getProjectId());
        map.put("projectName",project.getProjectName());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "认筹登记表");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    @PostMapping("/delete")
    @ApiOperation("删除认筹信息")
    public CommonResult delete(@RequestBody List<Long> identityLogIdList){
        identifyService.deleteBatch(identityLogIdList);
        return CommonResult.success();
    }


    @PostMapping("/updateSellstatus")
    @ApiOperation("修改认筹买售状态")
    public CommonResult updateSellStatus(Long id,Integer sellStatus){
        identifyService.updateSellStatus(id,sellStatus);
        return  CommonResult.success();
    }


    @PostMapping(value = "/import/{projectId}")
    @ApiOperation("导入数据")
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
