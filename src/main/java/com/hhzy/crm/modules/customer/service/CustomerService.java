package com.hhzy.crm.modules.customer.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.customer.dataobject.dto.CustomerDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.CustomerImport;
import com.hhzy.crm.modules.customer.entity.Customer;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 11:20
 * @Description:
 */
public interface CustomerService extends BaseService<Customer> {

    /**
     * 保存客户基本信息
     * @param customer
     */
    void saveBasicCustomer(Customer customer);

    void updateBasicCustomer(Customer customer);

    /**
     * 查询自己的客户
     * @param keyWord
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PageInfo<Customer> selectMyselfCustomer(String keyWord,Long projectId,Long userId, Integer page, Integer pageSize);


    List<Customer> selectCustomerByMobile(String mobile,Long projectId);

    /**
     * 查询客户详细信息
     * @param CustomerId
     * @return
     */
    Customer selectCusomerInfo(Long CustomerId);

    void deleteBatch(List<Long> customerIdList);

    void updateCustomerUser(Long customerId,Long userId);

    void updateUserBatch(UserBatchDTO userBatchDTO);

    /**
     * 查询客户列表
     * @param customerDTO
     * @return
     */
    PageInfo<Customer> selectAllCustomer(CustomerDTO customerDTO);

    int countToday(Long projectIds);


    Map<String,Object> importDatas(List<CustomerImport> list,Long projectId);

    List<Customer> selectAllCustomerByProjectId(Long projectId);

    /**
     * 小程序删除客户 把 置业顾问变为null
     */

    void removeUserId(List<Long> customerIdList);
}
