package com.hhzy.crm.modules.customer.controller.app;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.enums.HouseStatusEnum;
import com.hhzy.crm.common.enums.OfferBuyStatusEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.customer.entity.House;
import com.hhzy.crm.modules.customer.entity.OfferBuy;
import com.hhzy.crm.modules.customer.service.HouseService;
import com.hhzy.crm.modules.customer.service.OfferBuyService;
import com.hhzy.crm.modules.sys.service.ShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 10:11
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/app/offerbuy")
@Api(description = "小程序端认购登记管理")
public class AppOfferBuyController extends BaseController {

    @Autowired
    private OfferBuyService offerBuyService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private ShiroService shiroService;


    @PostMapping("/save")
    @ApiOperation("保存认购记录")
    public CommonResult save(@RequestBody OfferBuy offerBuy){
        Long userId = getUserId();
        offerBuy.setUserId(userId);
        offerBuy.setStatus(OfferBuyStatusEnum.UNCHECKED.getCode());
        offerBuy.setPrePrice(null);
        if (offerBuy.getHouseId()==null){
            throw  new BusinessException("请选择房屋");
        }
        House house = houseService.selectHouseInfo(offerBuy.getHouseId());
        if (!HouseStatusEnum.SELLING.getCode().equals(house.getStatus())){
            throw  new BusinessException("房屋为已售或者没开卖,请重新选择");
        }
        String mobile = offerBuy.getMobile();
        if (StringUtils.isEmpty(mobile)||mobile.length()!=11){
            throw  new BusinessException("请正确填写手机号");
        }else {
           /* Pattern pattern = Pattern.compile(CrmConstant.phoneRegex);
            boolean matches = pattern.matcher(mobile).matches();
            if (matches){*/
           offerBuyService.save(offerBuy);
           /* }else {
                throw  new BusinessException("请正确填写手机号");
            }*/
        }
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新认购记录")
    public CommonResult update(@RequestBody OfferBuy offerBuy){
        Long userId = getUserId();
        Long id = offerBuy.getId();
        OfferBuy buy = offerBuyService.queryById(id);
        if (OfferBuyStatusEnum.CHECKED.getCode().equals(buy.getStatus())){
            throw new BusinessException("该认购信息已审核通过,不能修改");
        }
        offerBuy.setUserId(userId);
        offerBuy.setPrePrice(null);
        if (offerBuy.getHouseId()==null){
            throw  new BusinessException("请选择房屋");
        }
        House house = houseService.selectHouseInfo(offerBuy.getHouseId());
        if (!HouseStatusEnum.SELLING.getCode().equals(house.getStatus())){
            throw  new BusinessException("房屋为已售或者没开卖,请重新选择");
        }
        offerBuy.setStatus(OfferBuyStatusEnum.UNCHECKED.getCode());
        offerBuyService.update(offerBuy);
        return CommonResult.success();
    }


    @PostMapping("/info/{id}")
    @ApiOperation("认购记录详情")
    public CommonResult info(@PathVariable Long id){
        OfferBuy offerBuy = offerBuyService.selectById(id);
        return CommonResult.success(offerBuy);
    }

    @PostMapping("/list")
    @ApiOperation("认购记录列表")
    public CommonResult list(String keyWord,Long projectId,Integer page,Integer pageSize) {
        Long userId = getUserId();
        Set<String> userPermissions = shiroService.getUserPermissions(userId);
        if (userPermissions.contains(CrmConstant.Permissions.LOOKOTHER)){
            userId=null;
        }
        PageInfo<OfferBuy> offerBuyPageInfo = offerBuyService.selectByName(keyWord, userId,projectId,page, pageSize);
        return CommonResult.success(offerBuyPageInfo);
    }



    @PostMapping("/delete")
    @ApiOperation("删除认购信息(软删除 是把userId 置为空)")
    @DataLog(value = "认购信息删除",actionType =CrmConstant.ActionType.DELETE,client = CrmConstant.Client.APP)
    public CommonResult delete(@RequestBody List<Long> offBuyIdList){
        offerBuyService.removeUserId(offBuyIdList);
        return CommonResult.success();
    }




}
