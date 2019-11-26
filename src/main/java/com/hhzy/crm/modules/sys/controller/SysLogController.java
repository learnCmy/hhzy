package com.hhzy.crm.modules.sys.controller;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.sys.dataobject.vo.SysLogDTO;
import com.hhzy.crm.modules.sys.entity.SysLog;
import com.hhzy.crm.modules.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: cmy
 * @Date: 2019/11/23 16:14
 * @Description:
 */
@RestController("/sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @PostMapping("/list")
    public CommonResult list(@RequestBody SysLogDTO sysLogDTO){
        PageInfo<SysLog> sysLogPageInfo = sysLogService.selectSysLog(sysLogDTO);
        return CommonResult.success(sysLogPageInfo);
    }

}
