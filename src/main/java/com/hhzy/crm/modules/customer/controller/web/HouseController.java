package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.enums.HouseStatusEnum;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.EnumUtil;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.HouseDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.HouseImport;
import com.hhzy.crm.modules.customer.entity.House;
import com.hhzy.crm.modules.customer.entity.Project;
import com.hhzy.crm.modules.customer.service.HouseService;
import com.hhzy.crm.modules.customer.service.ProjectService;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.ShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@Api(description = "网页端房屋管理接口")
@RequestMapping("/web/house")
public class HouseController extends BaseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ShiroService shiroService;

    @PostMapping("/save")
    @ApiOperation("新增房屋信息")
    @RequiresPermissions(value = "housesave")
    public CommonResult save(@RequestBody House house){
        houseService.save(house);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新房屋信息")
    @RequiresPermissions(value = "houseupdate")
    @DataLog(value = "更新房屋信息",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult update(@RequestBody House house){
        houseService.update(house);
        return CommonResult.success();
    }


    @PostMapping("/update/status")
    @ApiOperation("更新房屋售卖状态")
    @DataLog(value = "更新房屋售卖状态",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult update(Long houseId,Integer status){
        houseService.updateStatus(houseId,status);
        return CommonResult.success();
    }


    @PostMapping("/update/batch/status")
    @ApiOperation("批量更新房屋售卖状态")
    @DataLog(value = "更新房屋售卖状态",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateStatusBatch(@RequestBody House house){
        List<Long> ids = house.getIds();
        Integer status = house.getStatus();
        if (CollectionUtils.isEmpty(ids)||status==null){
            return CommonResult.success();
        }
        houseService.updateBatchStatus(ids,status);
        return CommonResult.success();
    }


    @PostMapping("/select")
    @ApiOperation("查询房屋列表")
    public CommonResult selectHouse(@RequestBody HouseDTO houseDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(houseDTO.getSortClause());
        houseDTO.setSortClause(sortClause);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if (userPermissions.contains(CrmConstant.Permissions.SHOP)&&!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)){
            houseDTO.setHouseTypePermission(2);
        }else if (userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            houseDTO.setHouseTypePermission(1);
        }else if (!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            return CommonResult.success(new PageInfo<House>());
        }
        PageInfo<House> housePageInfo = houseService.selectHouse(houseDTO);
        return  CommonResult.success(housePageInfo);
    }

    @PostMapping("/info/{houseId}")
    @ApiOperation("房屋详细信息")
    public  CommonResult selectHouseInfo(@PathVariable  Long houseId){
        House house = houseService.selectHouseInfo(houseId);
        return  CommonResult.success(house);
    }


    @PostMapping("/delete")
    @ApiOperation("删除房屋")
    @RequiresPermissions("delete")
    @DataLog(value = "删除房屋",actionType =CrmConstant.ActionType.DELETE)
    public CommonResult delete(@RequestBody List<Long> ids){
        houseService.deleteBatch(ids);
        return CommonResult.success();
    }


    @PostMapping(value = "/import/{projectId}/{type}")
    @ApiOperation("导入房屋数据")
    @RequiresPermissions("export")
    @DataLog(value = "导入房屋数据",actionType =CrmConstant.ActionType.IMPORT)
    public CommonResult excelImport(@PathVariable(value = "projectId") Long projectId,@PathVariable(value ="type")Integer type, @RequestParam("file")MultipartFile file){
        ImportParams importParams = new ImportParams();
        importParams.setTitleRows(1);
        List<HouseImport> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), HouseImport.class, importParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = houseService.importDatas(projectId, list, type);
        return CommonResult.success(map);
    }




    @GetMapping(value = "/downloadTemplate/{type}")
    @ApiOperation("导入模板下载 type 1 为住宅 2 为商铺")
    @RequiresPermissions("export")
    @DataLog(value = "(网页)房屋模板下载",actionType =CrmConstant.ActionType.EXPORT)
    public void download(@PathVariable Integer type){
        try (
                InputStream zhuzhaiStream =    getClass().getClassLoader().getResourceAsStream("excel-template/zhuzhaiTemplate.xlsx");
                InputStream shangpuAsStream = getClass().getClassLoader().getResourceAsStream("excel-template/shangpuTemplate.xlsx");
             OutputStream outputStream=response.getOutputStream();
        ){
            response.setContentType("application/x-down");
            if (type==1){
                response.addHeader("Content-Disposition","attachment;filename="+new String("住宅导入模板.xlsx".getBytes("gb2312"),"iso-8859-1"));
                IOUtils.copy(zhuzhaiStream,outputStream);
            }else if (type==2){
                response.addHeader("Content-Disposition","attachment;filename="+new String("商铺导入模板.xlsx".getBytes("gb2312"),"iso-8859-1"));
                IOUtils.copy(shangpuAsStream,outputStream);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @GetMapping("/export")
    @ApiOperation("房屋导出")
    @RequiresPermissions("export")
    @DataLog(value = "房屋数据导出",actionType =CrmConstant.ActionType.EXPORT)
    public void export(HouseDTO houseDTO,ModelMap modelMap) throws UnsupportedEncodingException {
        houseDTO.setSortClause("type,name,build_no,floor_level,room_no");
        houseDTO.setSort("asc");
        houseDTO.setPage(null);
        houseDTO.setPageSize(null);
        PageInfo<House> housePageInfo = houseService.selectHouse(houseDTO);
        List<House> list = housePageInfo.getList();
        for (House house : list) {
            Integer status = house.getStatus();
            HouseStatusEnum houseStatusEnum = EnumUtil.getByCode(status, HouseStatusEnum.class);
            if (houseStatusEnum!=null){
                house.setStatusStr(houseStatusEnum.getMessage());
            }
            Integer type = house.getType();
            if (type!=null){
                house.setTypeStr(type==1?"住宅":"商铺");
            }

        }
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/house.xlsx");
        map.put("list",list);
        Project project = projectService.queryById(houseDTO.getProjectId());
        map.put("projectName",project.getProjectName());
        modelMap.put(TemplateExcelConstants.FILE_NAME, URLEncoder.encode("房屋信息"+new DateTime().toString("yyyyMMdd"),"UTF-8"));
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }



}
