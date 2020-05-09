package com.hhzy.crm.modules.customer.controller.web;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dao.OfferBuyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.OfferBuyDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.OfferBuy;
import com.hhzy.crm.modules.customer.service.HouseService;
import com.hhzy.crm.modules.customer.service.OfferBuyService;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.ShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: cmy
 * @Date: 2019/8/28 16:41
 * @Description:
 */
@RestController
@Api(description = "网页端认筹管理")
@RequestMapping("/web/offerbuy")
public class OfferBuyController extends BaseController {

    @Autowired
    private OfferBuyService offerBuyService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private OfferBuyMapper offerBuyMapper;

    @PostMapping("/update/preprice")
    @ApiOperation("修改定金")
    @RequiresPermissions(value = {"buyprice","check"},logical = Logical.OR)
    @DataLog(value = "认购记录修改定价",actionType =CrmConstant.ActionType.UPDATE)
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
    @RequiresPermissions("buyselect")
    public CommonResult  select(@RequestBody OfferBuyDTO offerBuyDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(offerBuyDTO.getSortClause());
        offerBuyDTO.setSortClause(sortClause);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if (userPermissions.contains(CrmConstant.Permissions.SHOP)&&!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)){
            offerBuyDTO.setHouseType(2);
        }else if (userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            offerBuyDTO.setHouseType(1);
        }else if (!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            return CommonResult.success(new PageInfo<OfferBuy>());
        }
        PageInfo<OfferBuy> offerBuyPageInfo = offerBuyService.selectList(offerBuyDTO);
        if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
            offerBuyPageInfo.getList().forEach(e->e.setMobile(null));
        }
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
    @DataLog(value = "认购记录修改审核状态",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateStatus(Long id,Integer status,String refuseRemark){
        offerBuyService.updateStatus(id,status,refuseRemark);
        return CommonResult.success();
    }


    @PostMapping("/update/user")
    @ApiOperation("修改置业顾问")
    @RequiresPermissions("updateuser")
    @DataLog(value = "认购记录修改置业顾问",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateUser(Long id, Long userId){
        offerBuyService.updateUser(id,userId);
        return  CommonResult.success();

    }

    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    @DataLog(value = "认购记录批量修改置业顾问",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateUserBatch(@RequestBody UserBatchDTO userBatchDTO){
        offerBuyService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新认购记录")
    @RequiresPermissions(value="buy:update")
    @DataLog(value = "更新认购记录",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult update(@RequestBody OfferBuy offerBuy){
        offerBuyService.updateSelective(offerBuy);
        return CommonResult.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除认购信息")
    @RequiresPermissions("delete")
    @DataLog(value = "删除认购记录",actionType =CrmConstant.ActionType.DELETE)
    public CommonResult delete(@RequestBody List<Long> offBuyIdList){
        offerBuyService.deleteBatchForWeb(offBuyIdList);
        return CommonResult.success();
    }



}
