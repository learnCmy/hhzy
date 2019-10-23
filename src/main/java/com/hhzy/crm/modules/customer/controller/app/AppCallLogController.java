package com.hhzy.crm.modules.customer.controller.app;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.customer.dataobject.dto.CallLogDTO;
import com.hhzy.crm.modules.customer.entity.CallLog;
import com.hhzy.crm.modules.customer.service.CallLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/10 20:07
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/app/call")
@Api(description = "小程序端来电登记管理")
public class AppCallLogController extends BaseController {


    @Autowired
    private CallLogService callLogService;


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


    @PostMapping("/update")
    @ApiOperation("更新来电登记")
    public CommonResult updateCall(@RequestBody CallLog callLog){
        if (callLog.getProjectId()==null){
            throw  new BusinessException("房源项目Id不存在");
        }
        Long userId = getUserId();
        callLog.setUserId(userId);
        callLogService.updateCallLog(callLog);
        return CommonResult.success();
    }

    @PostMapping("/info/{id}")
    @ApiOperation("来电登记详情")
    public CommonResult info(@PathVariable Long id){
        CallLog callLog = callLogService.selectCallLogById(id);
        return CommonResult.success(callLog);
    }

    @PostMapping("list")
    @ApiOperation("来电登记列表")
    public CommonResult list(@RequestBody CallLogDTO callLogDTO){
        Long userId = getUserId();
        callLogDTO.setUserId(userId);
        callLogDTO.setSortClause("call_time");
        callLogDTO.setSort("desc");
        PageInfo<CallLog> callLogPageInfo = callLogService.selectList(callLogDTO);
        return CommonResult.success(callLogPageInfo);
    }


    @PostMapping("/delete")
    @ApiOperation("/删除来电登记(软删除 是把userId 置为空)")
    public CommonResult delete(@RequestBody List<Long> callLogIdList){
        callLogService.removeUserId(callLogIdList);
        return CommonResult.success();
    }


}
