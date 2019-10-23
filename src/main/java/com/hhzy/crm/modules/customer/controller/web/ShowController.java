package com.hhzy.crm.modules.customer.controller.web;

import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.customer.dataobject.dto.ShowDTO;
import com.hhzy.crm.modules.customer.dataobject.vo.Top10CusomterVO;
import com.hhzy.crm.modules.customer.dataobject.vo.Top10OfferBuyVo;
import com.hhzy.crm.modules.customer.service.ShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/9/24 22:15
 * @Description:
 */
@RestController
@Api(description = "工作台")
@RequestMapping("/web/show")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping("/count")
    @ApiOperation("访问统计")
    @RequiresPermissions("workbench")
    public CommonResult count(@RequestBody ShowDTO showDTO){
        Map<String, Object> map = showService.count(showDTO);
        return CommonResult.success(map);
    }


    @PostMapping("/count/sell")
    @ApiOperation("销售总额")
    @RequiresPermissions("workbench")
    public CommonResult countSellAmount(Long projectId){
        Map<String, Object> map = showService.coutSell(projectId);
        return CommonResult.success(map);
    }

    @PostMapping("/top/customer")
    @ApiOperation("前10的来访客户数的置业顾问")
    public CommonResult top10Customer(Long projectId){
        List<Top10CusomterVO> top10CusomterVOS = showService.listTop10Customer(projectId);
        return CommonResult.success(top10CusomterVOS);
    }

    @PostMapping("/top/offer")
    @ApiOperation("前10认购数的置业顾问")
    public CommonResult top10OfferBuyCustomer(Long projectId){
        List<Top10OfferBuyVo> top10OfferBuyVos = showService.listTop10OfferBuy(projectId);
        return CommonResult.success(top10OfferBuyVos);
    }

    @PostMapping("/line/chart")
    @ApiOperation("统计一星期的折线图'")
    public CommonResult line(Long projectId){
        Map<String, Object> map = showService.lineChart(projectId);
        return CommonResult.success(map);
    }
}
