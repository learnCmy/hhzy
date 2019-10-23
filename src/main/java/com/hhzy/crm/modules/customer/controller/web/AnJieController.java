package com.hhzy.crm.modules.customer.controller.web;

import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.customer.entity.SignInfo;
import com.hhzy.crm.modules.customer.service.SignInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 10:33
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("web/anjie")
@Api(description = "网页端按揭管理")
public class AnJieController extends BaseController {

    @Autowired
    private SignInfoService signInfoService;


    @PostMapping("/update")
    @ApiOperation("更新或录入 按揭信息")
    @RequiresPermissions("anjie")
    public CommonResult update(@RequestBody SignInfo signInfo){
        signInfoService.updateOrSaveAnjie(signInfo);
        return CommonResult.success();
    }


}
