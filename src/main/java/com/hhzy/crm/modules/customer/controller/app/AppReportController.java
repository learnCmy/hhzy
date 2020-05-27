package com.hhzy.crm.modules.customer.controller.app;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.ReportDTO;
import com.hhzy.crm.modules.customer.entity.Report;
import com.hhzy.crm.modules.customer.service.ReportService;
import com.hhzy.crm.modules.sys.service.ShiroService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2020/5/21 20:12
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/app/report")
public class AppReportController  extends BaseController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ShiroService shiroService;

    @PostMapping("/save")
    @ApiOperation("录入客户报备")
    public CommonResult saveReport(@RequestBody Report report){
        Long userId = getUserId();
        report.setUserId(userId);
        reportService.saveBasicReport(report);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新客户报备用户")
    public CommonResult updateReport(@RequestBody Report report){
        report.setUserId(getUserId());
        reportService.updateBasicReport(report);
        return CommonResult.success();
    }

    @PostMapping("/info/{id}")
    @ApiOperation("客户报备详细信息")
    public CommonResult info(@PathVariable Long id){
        Report report = reportService.queryById(id);
        return  CommonResult.success(report);
    }

    @PostMapping("/list")
    @ApiOperation("客户报备列表")
    public CommonResult list(@RequestBody ReportDTO reportDTO){
        Long userId = getUserId();
        reportDTO.setUserId(userId);
        String sortClause = StringHandleUtils.camel2UnderMultipleline(reportDTO.getSortClause());
        reportDTO.setSortClause(sortClause);
        PageInfo<Report> reportPageInfo = reportService.selectList(reportDTO);
        return  CommonResult.success(reportPageInfo);
    }

    @PostMapping("/delete")
    @ApiOperation("删除客户报备信息(软删除 是把userId 置为空)")
    @DataLog(value = "拓展信息删除",actionType =CrmConstant.ActionType.DELETE,client = CrmConstant.Client.APP)
    public CommonResult delete(@RequestBody List<Long> ids){
        reportService.removeUserId(ids);
        return CommonResult.success();
    }


}
