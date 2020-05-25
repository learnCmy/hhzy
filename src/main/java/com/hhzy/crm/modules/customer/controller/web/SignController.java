package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.enums.HouseStatusEnum;
import com.hhzy.crm.common.enums.SourceWayEnum;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.EnumUtil;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.ClearDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.SignDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.SignInfoDTO;
import com.hhzy.crm.modules.customer.dataobject.vo.SignVo;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.entity.SignInfo;
import com.hhzy.crm.modules.customer.service.ProjectService;
import com.hhzy.crm.modules.customer.service.SignInfoService;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.ShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Auther: cmy
 * @Date: 2019/8/28 16:40
 * @Description:
 */
@RestController
@Api(description = "网页端签约管理")
@RequestMapping("/web/sign")
public class SignController extends BaseController {


    @Autowired
    private SignInfoService signInfoService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ShiroService shiroService;


    @PostMapping("/save")
    @ApiOperation("保存签约信息")
    @RequiresPermissions(value = {"signupdate"},logical = Logical.OR)
    public CommonResult save(@RequestBody SignInfo signInfo){
        signInfoService.saveSelective(signInfo);
        return CommonResult.success();
    }

    @GetMapping("/info/{id}")
    @ApiOperation("签约信息详情")
    public CommonResult info(@PathVariable Long id){
        SignInfo signInfo = signInfoService.queryById(id);
        return CommonResult.success(signInfo);
    }


    @PostMapping("/update")
    @ApiOperation("更新签约")
    @RequiresPermissions(value = {"signupdate","anjie"},logical = Logical.OR)
    @DataLog(value = "更新签约",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult update(@RequestBody SignInfoDTO signInfoDTO){
        SignInfo signInfo = signInfoService.queryById(signInfoDTO.getId());
        BeanUtils.copyProperties(signInfoDTO,signInfo);
        signInfoService.update(signInfo);
        return CommonResult.success();
    }

    @PostMapping("/update/clear")
    @ApiOperation("更新结算信息")
    @RequiresPermissions(value = {"signupdate","anjie"},logical = Logical.OR)
    @DataLog(value = "更新结算",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateClear(@RequestBody ClearDTO clearDTO){
        SignInfo signInfo = signInfoService.queryById(clearDTO.getSignId());
        BeanUtils.copyProperties(clearDTO,signInfo);
        signInfoService.update(signInfo);
        return CommonResult.success();
    }



    @PostMapping("/list")
    @ApiOperation("签约记录列表")
    @RequiresPermissions(value = "signselect")
    public CommonResult list(@RequestBody SignDTO signDTO) {
        String sortClause = StringHandleUtils.camel2UnderMultipleline(signDTO.getSortClause());
        signDTO.setSortClause(sortClause);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if (userPermissions.contains(CrmConstant.Permissions.SHOP)&&!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)){
            signDTO.setHouseType(2);
        }else if (userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            signDTO.setHouseType(1);
        }else if (!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            return CommonResult.success(new PageInfo<SignVo>());
        }
        PageInfo<SignVo> signVoPageInfo = signInfoService.selectSignVo(signDTO);
        if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
            signVoPageInfo.getList().forEach(e->e.setMobile(null));
        }
        return CommonResult.success(signVoPageInfo);
    }


    @PostMapping("/delete")
    @ApiOperation("删除签约信息")
    @RequiresPermissions("delete")
    @DataLog(value = "签约信息删除",actionType =CrmConstant.ActionType.DELETE)
    public CommonResult delete(@RequestBody List<Long> signIdList){
        signInfoService.deleteBatch(signIdList);
        return CommonResult.success();
    }


    @GetMapping("/confirm")
    @ApiOperation("确认签约信息")
    @RequiresPermissions(value = {"signupdate","anjie"},logical = Logical.OR)
    public CommonResult confirm(Long signId,Boolean confirmInfo){
        SignInfo signInfo = new SignInfo();
        signInfo.setId(signId);
        signInfo.setUpdateTime(new Date());
        signInfo.setConfirmInfo(confirmInfo);
        signInfoService.updateSelective(signInfo);
        return  CommonResult.success();
    }

    @GetMapping("/export/shop")
    @ApiOperation("/导出商铺订购信息表")
    @RequiresPermissions("export")
    @DataLog(value = "商铺订购信息表导出",actionType =CrmConstant.ActionType.EXPORT)
    public void exportshangpu(SignDTO signDTO,ModelMap modelMap) throws UnsupportedEncodingException {
        signDTO.setHouseType(2);
        //String sortClause = StringHandleUtils.camel2UnderMultipleline(signDTO.getSortClause());
        //signDTO.setSortClause(sortClause);
        signDTO.setSortClause("offer_buy_time desc,sign_time");
        signDTO.setSort("desc");
        signDTO.setPage(null);
        signDTO.setPageSize(null);
        PageInfo<SignVo> signVoPageInfo = signInfoService.selectSignVoExport(signDTO);
        List<SignVo> list = signVoPageInfo.getList();
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        this.handleSingVO(list,userPermissions);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list",list);
        Project project = projectService.queryById(signDTO.getProjectId());
        map.put("projectName",project.getProjectName());
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/sign2.xlsx");
        String yyyyMMdd = new DateTime().toString("yyyyMMdd");
        modelMap.put(TemplateExcelConstants.FILE_NAME,URLEncoder.encode("商铺订购信息"+yyyyMMdd,"UTF-8") );
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }


    @GetMapping("/export")
    @ApiOperation("/导出住宅订购信息表")
    @RequiresPermissions("export")
    @DataLog(value = "住宅订购信息表导出",actionType =CrmConstant.ActionType.EXPORT)
    public void exportzhuzhai(SignDTO signDTO,ModelMap modelMap) throws UnsupportedEncodingException {
        signDTO.setHouseType(1);
        //String sortClause = StringHandleUtils.camel2UnderMultipleline(signDTO.getSortClause());
        //signDTO.setSortClause(sortClause);
        signDTO.setSortClause("offer_buy_time desc,sign_time");
        signDTO.setSort("desc");
        signDTO.setPage(null);
        signDTO.setPageSize(null);
        PageInfo<SignVo> signVoPageInfo = signInfoService.selectSignVoExport(signDTO);
        Project project = projectService.queryById(signDTO.getProjectId());
        Map<String, Object> map = new HashMap<String, Object>();
        List<SignVo> list = signVoPageInfo.getList();
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        this.handleSingVO(list,userPermissions);
        map.put("list",list);
        map.put("projectName",project.getProjectName());
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/sign1.xlsx");
        String yyyyMMdd = new DateTime().toString("yyyyMMdd");
        modelMap.put(TemplateExcelConstants.FILE_NAME,URLEncoder.encode("住宅订购信息"+yyyyMMdd,"UTF-8"));
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }



    private void handleSingVO(List<SignVo> list,Set<String> userPermissions){
        for (SignVo signVo : list) {
            HouseStatusEnum houseStatusEnum = EnumUtil.getByCode(signVo.getHouseStatus(), HouseStatusEnum.class);
            signVo.setHouseStatusStr(houseStatusEnum==null?null:houseStatusEnum.getMessage());
            SourceWayEnum sourceWayEnum = EnumUtil.getByCode(signVo.getSourceWay(), SourceWayEnum.class);
            signVo.setSourceWayStr(sourceWayEnum==null?null:sourceWayEnum.getMessage());
            if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
                signVo.setMobile(null);
            }
           if (signVo.getIsSubmit()!=null&&signVo.getIsSubmit()){
               signVo.setIsSubmitStr("是");
           }else {
               signVo.setIsSubmitStr("否");
           }
           if (signVo.getIsRecord()!=null&&signVo.getIsRecord()){
               signVo.setIsRecordStr("是");
           }else {
               signVo.setIsRecordStr("否");
           }

            if (signVo.getIsNetSign()!=null&&signVo.getIsNetSign()){
                signVo.setIsNetSignStr("是");
            }else {
                signVo.setIsNetSignStr("否");
            }

            if (signVo.getCommission()!=null&&signVo.getCommission()){
                signVo.setCommissionStr("是");
            }else {
                signVo.setCommissionStr("否");
            }


        }
    }
}
