package com.hhzy.crm.modules.customer.controller.app;

import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.customer.entity.Tookeen;
import com.hhzy.crm.modules.customer.service.TookeenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: cmy
 * @Date: 2019/10/21 19:48
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/app/tookeen")
@Api(description = "小程序端拓展登记管理")
public class AppTookeenController extends BaseController {

    @Autowired
    private TookeenService tookeenService;

    @PostMapping("/save")
    @ApiOperation("录入客户")
    public CommonResult saveTookeen(@RequestBody Tookeen tookeen){
        return CommonResult.success(tookeen);
    }

    @PostMapping("/update")
    @ApiOperation("更新拓展客户信息")
    public CommonResult updateTookeen(@RequestBody Tookeen tookeen){
        return CommonResult.success();
    }

    @PostMapping("/info/{id}")
    @ApiOperation("拓展客户信息")
    public CommonResult info(@PathVariable Long id){
        return  CommonResult.success();
    }

    @PostMapping("/list")
    @ApiOperation("拓展客户列表")
    public CommonResult list(){
        return  CommonResult.success();
    }



}
