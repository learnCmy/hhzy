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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    @RequiresPermissions("customerselect")
    public CommonResult list(@RequestBody CustomerDTO customerDTO){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(customerDTO.getSortClause());
        customerDTO.setSortClause(sortClause);
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        if (userPermissions.contains(CrmConstant.Permissions.SHOP)&&!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)){
            customerDTO.setHouseTypePermission(2);
        }else if (userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            customerDTO.setHouseTypePermission(1);
        }else if (!userPermissions.contains(CrmConstant.Permissions.RESIDENCE)
                &&!userPermissions.contains(CrmConstant.Permissions.SHOP)){
            return CommonResult.success(new PageInfo<Customer>());
        }

        PageInfo<Customer> customerPageInfo = customerService.selectAllCustomer(customerDTO);
        if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
            customerPageInfo.getList().forEach(e->e.setMobile(null));
        }
        return  CommonResult.success(customerPageInfo);
    }


    @GetMapping("/update/user")
    @ApiOperation("修改职业顾问")
    @RequiresPermissions("updateuser")
    @DataLog(value = "(网页)来访客户修改置业顾问",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult list(Long customerId,Long userId){
        customerService.updateCustomerUser(customerId,userId);
        return  CommonResult.success();
    }



    @PostMapping("/update/user/batch")
    @ApiOperation("批量修改置业顾问")
    @RequiresPermissions("updateuser")
    @DataLog(value = "(网页)来访客户批量修改置业顾问",actionType =CrmConstant.ActionType.UPDATE)
    public CommonResult updateUSer(@RequestBody UserBatchDTO userBatchDTO){
        customerService.updateUserBatch(userBatchDTO);
        return CommonResult.success();
    }



    @PostMapping("/update")
    @ApiOperation("更新客户信息")
    @RequiresPermissions("customerupdate")
    @DataLog(value = "来访客户更新",actionType =CrmConstant.ActionType.UPDATE)
    public  CommonResult updateCustomer(@RequestBody Customer customer){
        customerService.updateBasicCustomer(customer);
        return CommonResult.success();
    }


    @PostMapping("/info/{customerId}")
    @ApiOperation("用户详细信息")
    public CommonResult info(@PathVariable Long customerId){
        Customer customer = customerService.selectCusomerInfo(customerId);
        return CommonResult.success(customer);
    }




    @GetMapping("/export")
    @ApiOperation("/来访客户登记导出")
    @RequiresPermissions("export")
    @DataLog(value = "来访客户导出",actionType =CrmConstant.ActionType.EXPORT)
    public void export(CustomerDTO customerDTO,ModelMap modelMap){
        String sortClause = StringHandleUtils.camel2UnderMultipleline(customerDTO.getSortClause());
        SysUser user = getUser();
        Set<String> userPermissions = shiroService.getUserPermissions(user.getUserId());
        customerDTO.setSortClause(sortClause);
        customerDTO.setPage(null);
        customerDTO.setPageSize(null);
        PageInfo<Customer> customerPageInfo = customerService.selectAllCustomer(customerDTO);
        List<Customer> list = customerPageInfo.getList();
        for (Customer customer : list) {
            SourceWayEnum sourceWayEnum = EnumUtil.getByCode(customer.getSourceWay(), SourceWayEnum.class);
            customer.setSourceWayStr(sourceWayEnum==null?null:sourceWayEnum.getMessage());
            if (userPermissions.contains(CrmConstant.Permissions.SENSITIVE)){
               customer.setMobile(null);
            }
            if (Integer.valueOf(1).equals(customer.getSex())){
                customer.setSexStr("男");
            }else {
                customer.setSexStr("女");
            }
            if (Integer.valueOf(1).equals(customer.getProductType())){
                customer.setProductTypeStr("住宅");
            }else if (Integer.valueOf(2).equals(customer.getProductType())){
                customer.setProductTypeStr("商铺");
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        TemplateExportParams params = new TemplateExportParams(
                "excel-template/customer.xlsx");
        map.put("list",list);
        String yyyyMMdd = new DateTime().toString("yyyyMMdd");
        String fileName="";
        if (customerDTO.getProductType()!=null){
            if (Integer.valueOf(1).equals(customerDTO.getProductType())){
                fileName= "住宅"+"来访客户登记表"+yyyyMMdd;
            }else if (Integer.valueOf(2).equals(customerDTO.getProductType())){
                fileName="商铺"+"来访客户登记表"+yyyyMMdd;
            }
        }else {
            fileName="来访客户登记表"+yyyyMMdd;
        }
        try {
            modelMap.put(TemplateExcelConstants.FILE_NAME, URLEncoder.encode(fileName,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            modelMap.put(TemplateExcelConstants.FILE_NAME,fileName);
        }
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String filename="来访客户登记表";
        System.out.println(new String(filename.getBytes("UTF-8")));
    }

    @PostMapping("/delete")
    @ApiOperation("/删除客户信息")
    @RequiresPermissions("delete")
    @DataLog(value = "来访删除",actionType =CrmConstant.ActionType.EXPORT)
    public CommonResult delete(@RequestBody List<Long> customerIdList){
        customerService.deleteBatch(customerIdList);
        return CommonResult.success();
    }


    @PostMapping(value = "/import/{projectId}")
    @ApiOperation("导入数据")
    @RequiresPermissions("export")
    @DataLog(value = "来访导入数据",actionType =CrmConstant.ActionType.IMPORT)
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
    @RequiresPermissions("export")
    @DataLog(value = "来访导入模板下载",actionType =CrmConstant.ActionType.EXPORT)
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