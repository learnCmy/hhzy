package com.hhzy.crm.modules.customer.controller.app;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.ValidatorUtils;
import com.hhzy.crm.modules.customer.entity.Customer;
import com.hhzy.crm.modules.customer.service.CustomerService;
import com.hhzy.crm.modules.sys.service.ShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 11:28
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/app/customer")
@Api(description = "小程序端来访客户管理")
public class AppCustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;


    @Autowired
    private ShiroService shiroService;

    @PostMapping("/save")
    @ApiOperation("录入客户")
    public CommonResult saveCustomer(@RequestBody Customer customer){
        ValidatorUtils.validateEntity(customer);
        Long userId = getUserId();
        customer.setUserId(userId);
        customerService.saveBasicCustomer(customer);
        return CommonResult.success();
    }

    @PostMapping("/update")
    @ApiOperation("更新客户信息")
    public  CommonResult updateCustomer(@RequestBody Customer customer){
        ValidatorUtils.validateEntity(customer);
        Long userId = getUserId();
        customer.setUserId(userId);
        customerService.updateBasicCustomer(customer);
        return CommonResult.success();
    }


    @PostMapping("/list")
    @ApiOperation("查询自己的客户信息列表")
    public CommonResult list(String keyWord, @RequestParam("projectId") Long projectId,@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize){
        Long userId = getUserId();
        Set<String> userPermissions = shiroService.getUserPermissions(userId);
        if (userPermissions.contains(CrmConstant.Permissions.LOOKOTHER)){
            userId=null;
        }
        PageInfo<Customer> customerPageInfo = customerService.selectMyselfCustomer(keyWord,projectId,userId, page, pageSize);
        return  CommonResult.success(customerPageInfo);
    }

    @PostMapping("/info/{customerId}")
    @ApiOperation("用户详细信息")
    public CommonResult info(@PathVariable Long customerId){
        Customer customer = customerService.selectCusomerInfo(customerId);
        return CommonResult.success(customer);
    }



    @PostMapping("/delete")
    @ApiOperation("/删除客户信息(软删除 是把userId 置为空)")
    public CommonResult delete(@RequestBody List<Long> customerIdList){
        customerService.removeUserId(customerIdList);
        return CommonResult.success();
    }


}
