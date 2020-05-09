package com.hhzy.crm.modules.customer.controller.web;

import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.customer.dataobject.dto.SignInfoDTO;
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
    @DataLog(value = "更新(按揭信息)",actionType =CrmConstant.ActionType.UPDATE)
    @RequiresPermissions("anjie")
    public CommonResult update(@RequestBody SignInfoDTO signInfoDTO){
        signInfoService.updateOrSaveAnjie(signInfoDTO);
        return CommonResult.success();
    }


}
