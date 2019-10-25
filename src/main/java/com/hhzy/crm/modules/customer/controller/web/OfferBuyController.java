package com.hhzy.crm.modules.customer.controller.web;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.OfferBuyDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.OfferBuy;
import com.hhzy.crm.modules.customer.service.HouseService;
import com.hhzy.crm.modules.customer.service.OfferBuyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/8/28 16:41
 * @Description:
 */
@RestController
@Api(description = "网页端认筹管理")
@RequestMapping("/web/offerbuy")
public class OfferBuyController {

    @Autowired
    private OfferBuyService offerBuyService;

    @Autowired
    private HouseService houseService;

    @PostMapping("/update/preprice")
    @ApiOperation("修改定金")
    public CommonResult updatePrePrice(@RequestBody OfferBuy offerBuy){
        offerBuyService.updatePrePrice(offerBuy);
        return CommonResult.success();
    }

    @GetMapping("/count/status")
    @ApiOperation("统计不同状态下的认购总数")
    public CommonResult countStatus(Long projectId){
        Map<String, Object> map = offerBuyService.countNumberByStatus(projectId);
        return CommonResult.success(map);
    }


    @PostMapping("/list")
    @ApiOperation("认购用户信息查询")
    public CommonResult  select(@RequestBody OfferBuyDTO offerBuyDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(offerBuyDTO.getSortClause());
        offerBuyDTO.setSortClause(sortClause);
        PageInfo<OfferBuy> offerBuyPageInfo = offerBuyService.selectList(offerBuyDTO);
        return CommonResult.success(offerBuyPageInfo);
    }


    @PostMapping("/info/{id}")
    @ApiOperation("认购记录详情")
    public CommonResult info(@PathVariable Long id){
        OfferBuy offerBuy = offerBuyService.selectById(id);
        return CommonResult.success(offerBuy);
    }


    @PostMapping("/update/status")
    @ApiOperation("修改审核状态")
    @RequiresPermissions("check")
    public CommonResult updateStatus(Long id,Integer status){
        offerBuyService.updateStatus(id,status);
        return CommonResult.success();
    }


    @PostMapping("/update/user")
    @ApiOperation("修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult updateUser(Long id, Long userId){
        offerBuyService.updateUser(id,userId);
        return  CommonResult.success();

    }

    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult updateUSer(@RequestBody UserBatchDTO userBatchDTO){
        offerBuyService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新认购记录")
    public CommonResult update(@RequestBody OfferBuy offerBuy){
        offerBuyService.update(offerBuy);
        return CommonResult.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除认购信息")
    public CommonResult delete(@RequestBody List<Long> offBuyIdList){
        offerBuyService.deleteBatchForWeb(offBuyIdList);
        return CommonResult.success();
    }



}
