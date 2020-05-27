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
import com.hhzy.crm.modules.customer.dataobject.dto.ReportDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.entity.Report;
import com.hhzy.crm.modules.customer.service.ProjectService;
import com.hhzy.crm.modules.customer.service.ReportService;
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
 * @Date: 2020/5/25 09:12
 * @Description:
 */
@RestController
@Api(description = "网页端客户报备管理 ")
@RequestMapping("/web/report")
public class ReportController extends BaseController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ProjectService projectService;


    @Autowired
    private ShiroService shiroService;


    @PostMapping("list")
    @ApiOperation("客户报备列表")
    @RequiresPermissions("reportselect")
    public CommonResult list(@RequestBody ReportDTO reportDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(reportDTO.getSortClause());
        reportDTO.setSortClause(sortClause);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        PageInfo<Report> reportPageInfo = reportService.selectList(reportDTO);
        if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
            reportPageInfo.getList().forEach(e->e.setMobile(null));
        }
        return CommonResult.success(reportPageInfo);
    }


    @PostMapping("/update/user")
    @ApiOperation("修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult updateUser(Long id, Long userId){
        reportService.updateUser(id,userId);
        return  CommonResult.success();
    }

    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult updateUSerBatch(@RequestBody UserBatchDTO userBatchDTO){
        reportService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除客户报备信息")
    @RequiresPermissions("delete")
    public CommonResult delete(@RequestBody List<Long> ids){
        reportService.deleteBatch(ids);
        return CommonResult.success();
    }


    @PostMapping("/info/{id}")
    @ApiOperation("客户报备详细信息")
    public CommonResult info(@PathVariable Long id){
        Report report = reportService.queryById(id);
        return  CommonResult.success(report);
    }


    @PostMapping("/update")
    @ApiOperation("更新客户报备信息")
    @RequiresPermissions("reportupdate")
    public CommonResult updateTookeen(@RequestBody Report report){
        reportService.updateBasicReport(report);
        return CommonResult.success();
    }




    @GetMapping("/export")
    @ApiOperation("/客户报备导出")
    @DataLog(value = "客户报备表导出",actionType =CrmConstant.ActionType.EXPORT)
    public void export(ReportDTO reportDTO, ModelMap modelMap) throws UnsupportedEncodingException {
        String sortCaluse = StringHandleUtils.camel2UnderMultipleline(reportDTO.getSortClause());
        reportDTO.setSortClause(sortCaluse);
        reportDTO.setPage(null);
        reportDTO.setPageSize(null);
        PageInfo<Report> reportPageInfo = reportService.selectList(reportDTO);
        List<Report> list = reportPageInfo.getList();
        for (int i = 0; i <list.size() ; i++) {
            Report report = list.get(i);
            report.setId(Long.valueOf(i+1));
            if (Integer.valueOf(1).equals(report.getSex())){
                report.setSexStr("男");
            }else {
                report.setSexStr("女");
            }
            if (Integer.valueOf(1).equals(report.getProductType())){
                report.setProductTypeStr("住宅");
            }else if (Integer.valueOf(2).equals(report.getProductType())){
                report.setProductTypeStr("商铺");
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/report.xlsx");
        map.put("list",list);
        Project project = projectService.queryById(reportDTO.getProjectId());
        map.put("projectName",project.getProjectName());
        String yyyyMMdd=new DateTime().toString("yyyyMMdd");
        String fileName="";
        if (reportDTO.getProductType()!=null){
            if (Integer.valueOf(1).equals(reportDTO.getProductType())){
                fileName= "住宅"+"客户报备登记表"+yyyyMMdd;
            }else if (Integer.valueOf(2).equals(reportDTO.getProductType())){
                fileName="商铺"+"客户报备登记表"+yyyyMMdd;
            }
        }else {
            fileName="客户报备登记表"+yyyyMMdd;
        }
        modelMap.put(TemplateExcelConstants.FILE_NAME, URLEncoder.encode(fileName,"UTF-8"));
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }



}
