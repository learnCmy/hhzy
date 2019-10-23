package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.HouseDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.HouseImport;
import com.hhzy.crm.modules.customer.entity.House;
import com.hhzy.crm.modules.customer.service.HouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


@RestController
@Api(description = "网页端房屋管理接口")
@RequestMapping("/web/house")
public class HouseController extends BaseController {

    @Autowired
    private HouseService houseService;

    @PostMapping("/save")
    @ApiOperation("新增房屋信息")
    public CommonResult save(@RequestBody House house){
        houseService.save(house);
        return CommonResult.success();
    }


    @PostMapping("/update")
    @ApiOperation("更新房屋信息")
    public CommonResult update(@RequestBody House house){
        houseService.update(house);
        return CommonResult.success();
    }


    @PostMapping("/update/status")
    @ApiOperation("更新房屋售卖状态")
    public CommonResult update(Long houseId,Integer status){
        houseService.updateStatus(houseId,status);
        return CommonResult.success();
    }


    @PostMapping("/select")
    @ApiOperation("查询房屋列表")
    public CommonResult selectHouse(@RequestBody HouseDTO houseDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(houseDTO.getSortClause());
        houseDTO.setSortClause(sortClause);
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
    public CommonResult delete(@RequestBody List<Long> ids){
        houseService.deleteBatch(ids);
        return CommonResult.success();
    }


    @PostMapping(value = "/import/{projectId}/{type}")
    @ApiOperation("导入房屋数据")
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











}
