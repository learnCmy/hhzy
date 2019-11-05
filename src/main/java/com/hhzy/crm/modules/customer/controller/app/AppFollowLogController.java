package com.hhzy.crm.modules.customer.controller.app;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.customer.entity.FollowLog;
import com.hhzy.crm.modules.customer.service.FollowLogService;
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
 * @Date: 2019/8/11 13:06
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/app/follow")
@Api(description = "小程序端跟进记录管理")
public class AppFollowLogController extends BaseController {

    @Autowired
    private FollowLogService followLogService;

    @Autowired
    private ShiroService shiroService;



    @PostMapping("/save")
    @ApiOperation("保存跟进记录")
    public CommonResult save(@RequestBody FollowLog followLog){
        if (followLog.getProjectId()==null){
            throw  new BusinessException("项目id不能为空");
        }
        Long userId = getUserId();
        followLog.setUserId(userId);
        followLogService.save(followLog);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新跟进记录")
    public CommonResult update(@RequestBody FollowLog followLog){
        if (followLog.getProjectId()==null){
            throw  new BusinessException("项目id不能为空");
        }
        followLog.setUserId(getUserId());
        followLogService.update(followLog);
        return CommonResult.success();
    }

    @PostMapping("/info/{id}")
    @ApiOperation("跟进记录详情")
    public CommonResult info(@PathVariable Long id){
        FollowLog followLog = followLogService.selectById(id);
        if (!followLog.getUserId().equals(getUserId())){
            throw new BusinessException("跟进记录的所属置业顾问不是您，请联系管理员");
        }
        return CommonResult.success(followLog);
    }


    @PostMapping("/searchByMobile")
    @ApiOperation("根据手机号查跟进")
    public CommonResult followInfo(Long projectId,String mobile){
        FollowLog followLog = followLogService.selectByMobile(projectId, mobile);
        return  CommonResult.success(followLog);
    }



    @PostMapping("/list")
    @ApiOperation("跟进记录列表")
    public CommonResult list(String keyWord,Long projectId,Integer page,Integer pageSize) {
        Long userId = getUserId();
        Set<String> userPermissions = shiroService.getUserPermissions(userId);
        if (userPermissions.contains(CrmConstant.Permissions.LOOKOTHER)){
            userId=null;
        }
        PageInfo<FollowLog> followLogPageInfo = followLogService.selcetByKeyWord(keyWord,projectId,userId, page, pageSize);
        return CommonResult.success(followLogPageInfo);
    }

    @PostMapping("/delete")
    @ApiOperation("删除跟进信息(软删除 是把userId 置为空)")
    public CommonResult delete(@RequestBody List<Long> followIdLogList){
       followLogService.removeUserId(followIdLogList);
        return CommonResult.success();
    }


}
