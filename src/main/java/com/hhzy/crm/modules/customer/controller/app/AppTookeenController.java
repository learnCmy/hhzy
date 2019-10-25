package com.hhzy.crm.modules.customer.controller.app;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.TookeenDTO;
import com.hhzy.crm.modules.customer.entity.Tookeen;
import com.hhzy.crm.modules.customer.service.TookeenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Long userId = getUserId();
        tookeen.setUserId(userId);
        tookeenService.saveBasicTookeen(tookeen);
        return CommonResult.success();
    }

    @PostMapping("/update")
    @ApiOperation("更新拓展客户信息")
    public CommonResult updateTookeen(@RequestBody Tookeen tookeen){
        tookeen.setUserId(getUserId());
        tookeenService.updateBasicTookeen(tookeen);
        return CommonResult.success();
    }

    @PostMapping("/info/{id}")
    @ApiOperation("拓展客户详细信息")
    public CommonResult info(@PathVariable Long id){
        Tookeen tookeen = tookeenService.queryById(id);
        return  CommonResult.success(tookeen);
    }

    @PostMapping("/list")
    @ApiOperation("拓展客户列表")
    public CommonResult list(@RequestBody TookeenDTO tookeenDTO){
        Long userId = getUserId();
        tookeenDTO.setUserId(userId);
        String sortClause = StringHandleUtils.camel2UnderMultipleline(tookeenDTO.getSortClause());
        tookeenDTO.setSortClause(sortClause);
        PageInfo<Tookeen> tookeenPageInfo = tookeenService.selectList(tookeenDTO);
        return  CommonResult.success(tookeenPageInfo);
    }

    @PostMapping("/delete")
    @ApiOperation("删除认筹信息(软删除 是把userId 置为空)")
    public CommonResult delete(@RequestBody List<Long> ids){
        tookeenService.removeUserId(ids);
        return CommonResult.success();
    }




}
