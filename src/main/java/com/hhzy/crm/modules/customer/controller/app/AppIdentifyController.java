package com.hhzy.crm.modules.customer.controller.app;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.enums.IdentifySellStatusEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.customer.entity.IdentifyLog;
import com.hhzy.crm.modules.customer.service.IdentifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 09:57
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/app/identify")
@Api(description = "小程序端认筹登记管理")
public class AppIdentifyController extends BaseController {


    @Autowired
    private IdentifyService identifyService;


    @PostMapping("/save")
    @ApiOperation("保存认筹记录")
    public CommonResult save(@RequestBody IdentifyLog identifyLog){
        if (identifyLog.getProjectId()==null){
            throw  new BusinessException("项目Id不能为空");
        }
        Long userId = getUserId();
        identifyLog.setUserId(userId);
        identifyLog.setSellStatus(IdentifySellStatusEnum.BUYCARD.getCode());
        identifyService.save(identifyLog);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新认筹记录")
    public CommonResult update(@RequestBody IdentifyLog identifyLog){
        if (identifyLog.getProjectId()==null){
            throw  new BusinessException("项目Id不能为空");
        }
        Long userId = getUserId();
        identifyLog.setUserId(userId);
        identifyService.updateByPrimaryKeySelective(identifyLog);
        return CommonResult.success();
    }


    @PostMapping("/info/{id}")
    @ApiOperation("认筹记录详情")
    public CommonResult info(@PathVariable Long id){
        IdentifyLog identifyLog = identifyService.selectById(id);
        return CommonResult.success(identifyLog);
    }


    @PostMapping("/list")
    @ApiOperation("认筹记录列表")
    public CommonResult list(String keyWord,@RequestParam Long projectId,Integer page,Integer pageSize) {
        Long userId = getUserId();
        PageInfo<IdentifyLog> identifyLogPageInfo = identifyService.selcetByKeyWord(keyWord,projectId,userId,page,pageSize);
        return CommonResult.success(identifyLogPageInfo);
    }


    @PostMapping("/delete")
    @ApiOperation("删除认筹信息(软删除 是把userId 置为空)")
    public CommonResult delete(@RequestBody List<Long> identityLogIdList){
        identifyService.removeUserId(identityLogIdList);
        return CommonResult.success();
    }


}
