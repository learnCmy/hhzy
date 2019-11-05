package com.hhzy.crm.modules.customer.controller.web;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseController;
import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.enums.SourceWayEnum;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.common.utils.EnumUtil;
import com.hhzy.crm.common.utils.StringHandleUtils;
import com.hhzy.crm.modules.customer.dataobject.dto.CustomerDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.CustomerImport;
import com.hhzy.crm.modules.customer.entity.Customer;
import com.hhzy.crm.modules.customer.service.CustomerService;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.ShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: cmy
 * @Date: 2019/8/9 17:26
 * @Description:
 */
@RestController
@Api(description = "网页端来访客户管理")
@RequestMapping("/web/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShiroService shiroService;

    @PostMapping("/list")
    @ApiOperation("查询所有客户信息")
    @RequiresPermissions("customer")
    public CommonResult list(@RequestBody CustomerDTO customerDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(customerDTO.getSortClause());
        customerDTO.setSortClause(sortClause);
        PageInfo<Customer> customerPageInfo = customerService.selectAllCustomer(customerDTO);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
            customerPageInfo.getList().forEach(e->e.setMobile(null));
        }
        return  CommonResult.success(customerPageInfo);
    }


    @GetMapping("/update/user")
    @ApiOperation("修改职业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult list(Long customerId,Long userId){
        customerService.updateCustomerUser(customerId,userId);
        return  CommonResult.success();
    }


    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    public CommonResult updateUSer(@RequestBody UserBatchDTO userBatchDTO){
        customerService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }



    @PostMapping("/update")
    @ApiOperation("更新客户信息")
    @RequiresPermissions("customer")
    public  CommonResult updateCustomer(@RequestBody Customer customer){
        customerService.updateBasicCustomer(customer);
        return CommonResult.success();
    }


    @PostMapping("/info/{customerId}")
    @ApiOperation("用户详细信息")
    @RequiresPermissions("customer")
    public CommonResult info(@PathVariable Long customerId){
        Customer customer = customerService.selectCusomerInfo(customerId);
        return CommonResult.success(customer);
    }




    @GetMapping("/export")
    @ApiOperation("/来访客户登记导出")
    public void export(ModelMap modelMap, CustomerDTO customerDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(customerDTO.getSortClause());
        customerDTO.setSortClause(sortClause);
        PageInfo<Customer> customerPageInfo = customerService.selectAllCustomer(customerDTO);
        List<Customer> list = customerPageInfo.getList();
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        for (Customer customer : list) {
            SourceWayEnum sourceWayEnum = EnumUtil.getByCode(customer.getSourceWay(), SourceWayEnum.class);
            customer.setSourceWayStr(sourceWayEnum==null?null:sourceWayEnum.getMessage());
            if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
               customer.setMobile(null);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/customer.xlsx");
        map.put("list",list);
        String yyyyMMdd = new DateTime().toString("yyyyMMdd");
        modelMap.put(TemplateExcelConstants.FILE_NAME, "来访客户登记表"+yyyyMMdd);
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }

    @PostMapping("/delete")
    @ApiOperation("/删除客户信息")
    public CommonResult delete(@RequestBody List<Long> customerIdList){
        customerService.deleteBatch(customerIdList);
        return CommonResult.success();
    }


    @PostMapping(value = "/import/{projectId}")
    @ApiOperation("导入数据")
    public CommonResult excelImport(@PathVariable(value = "projectId") Long projectId, @RequestParam("file")MultipartFile file){
        //File file1 = new File(getClass().getClassLoader().getResource("excel-template/customerTemplate.xlsx").getFile());
        ImportParams importParams = new ImportParams();
        importParams.setTitleRows(1);
        List<CustomerImport> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), CustomerImport.class, importParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = customerService.importDatas(list,projectId);
        return CommonResult.success(map);
    }

    @GetMapping(value = "/downloadTemplate")
    @ApiOperation("导入模板下载")
    public void download(){
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("excel-template/customerTemplate.xlsx");
             OutputStream outputStream=response.getOutputStream();
        ){
            response.setContentType("application/x-down");
            response.addHeader("Content-Disposition","attachment;filename="+new String("来访客户导入模板.xlsx".getBytes("gb2312"),"iso-8859-1"));
            IOUtils.copy(resourceAsStream,outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}