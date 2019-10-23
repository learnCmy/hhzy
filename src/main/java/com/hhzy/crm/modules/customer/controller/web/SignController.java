package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.enums.HouseStatusEnum;
import com.hhzy.crm.common.enums.SourceWayEnum;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.EnumUtil;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.SignDTO;
import com.hhzy.crm.modules.customer.dataobject.vo.SignVo;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.entity.SignInfo;
import com.hhzy.crm.modules.customer.service.ProjectService;
import com.hhzy.crm.modules.customer.service.SignInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @PostMapping("/save")
    @ApiOperation("保存签约信息")
    @RequiresPermissions("sign")
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
    @RequiresPermissions("sign")
    public CommonResult update(@RequestBody SignInfo signInfo){
        signInfoService.updateSelective(signInfo);
        return CommonResult.success();
    }

    @PostMapping("/list")
    @ApiOperation("签约记录列表")
    @RequiresPermissions("sign")
    public CommonResult list(@RequestBody SignDTO signDTO) {
        String sortClause = StringHandleUtils.camel2UnderMultipleline(signDTO.getSortClause());
        signDTO.setSortClause(sortClause);
        PageInfo<SignVo> signVoPageInfo = signInfoService.selectSignVo(signDTO);
        return CommonResult.success(signVoPageInfo);
    }



    @PostMapping("/delete")
    @ApiOperation("删除签约信息")
    @RequiresPermissions("sign")
    public CommonResult delete(@RequestBody List<Long> signIdList){
        signInfoService.deleteBatch(signIdList);
        return CommonResult.success();
    }



    @GetMapping("/export/shop")
    @ApiOperation("/导出商铺订购信息表")
    public void exportshangpu(ModelMap modelMap,SignDTO signDTO){
        signDTO.setHouseType(2);
        String sortClause = StringHandleUtils.camel2UnderMultipleline(signDTO.getSortClause());
        signDTO.setSortClause(sortClause);
        PageInfo<SignVo> signVoPageInfo = signInfoService.selectSignVo(signDTO);
        List<SignVo> list = signVoPageInfo.getList();
        this.handleSingVO(list);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list",list);
        Project project = projectService.queryById(signDTO.getProjectId());
        map.put("projectName",project.getProjectName());
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/sign2.xlsx");

        modelMap.put(TemplateExcelConstants.FILE_NAME, "商铺订购信息");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }


    @GetMapping("/export")
    @ApiOperation("/导出住宅订购信息表")
    public void exportzhuzhai(ModelMap modelMap,SignDTO signDTO){
        signDTO.setHouseType(1);
        String sortClause = StringHandleUtils.camel2UnderMultipleline(signDTO.getSortClause());
        signDTO.setSortClause(sortClause);
        PageInfo<SignVo> signVoPageInfo = signInfoService.selectSignVo(signDTO);
        Project project = projectService.queryById(signDTO.getProjectId());
        Map<String, Object> map = new HashMap<String, Object>();
        List<SignVo> list = signVoPageInfo.getList();
        this.handleSingVO(list);
        map.put("list",list);
        map.put("projectName",project.getProjectName());
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/sign1.xlsx");

        modelMap.put(TemplateExcelConstants.FILE_NAME, "住宅订购信息");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }



    private void handleSingVO(List<SignVo> list){
        for (SignVo signVo : list) {
            HouseStatusEnum houseStatusEnum = EnumUtil.getByCode(signVo.getHouseStatus(), HouseStatusEnum.class);
            signVo.setHouseStatusStr(houseStatusEnum==null?null:houseStatusEnum.getMessage());
            SourceWayEnum sourceWayEnum = EnumUtil.getByCode(signVo.getSourceWay(), SourceWayEnum.class);
            signVo.setSourceWayStr(sourceWayEnum==null?null:sourceWayEnum.getMessage());
        }
    }
}
