package com.hhzy.crm.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.common.enums.SourceWayEnum;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.common.utils.DateUtils;
import com.hhzy.crm.common.utils.EnumUtil;
import com.hhzy.crm.modules.customer.dao.CustomerMapper;
import com.hhzy.crm.modules.customer.dao.IdentifyLogMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.CustomerDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.CustomerImport;
import com.hhzy.crm.modules.customer.entity.Customer;
import com.hhzy.crm.modules.customer.entity.FollowLog;
import com.hhzy.crm.modules.customer.service.CustomerService;
import com.hhzy.crm.modules.customer.service.FollowLogService;
import com.hhzy.crm.modules.sys.dao.SysUserDao;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotBlank;
import java.sql.Ref;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 11:21
 * @Description:
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements CustomerService  {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private FollowLogService followLogService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserDao sysUserDao;


    @Override
    public void saveBasicCustomer(Customer customer) {
        if (StringUtils.isEmpty(customer.getMobile())){
            throw  new BusinessException("手机号不能为空");
        }
        Example example = new Example(Customer.class);
        example.createCriteria().andEqualTo("mobile",customer.getMobile())
                                .andEqualTo("projectId",customer.getProjectId());
        List<Customer> customers = customerMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(customers)){
            String username="";
            Long userId = customers.get(0).getUserId();
            SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
            if (sysUser!=null){
                username = sysUser.getUsername();
            }
            throw new BusinessException("【手机号已存在】该客户信息已被【"+username+"】录入");
        }
        this.saveSelective(customer);
    }

    @Override
    public void updateBasicCustomer(Customer customer) {
        if (StringUtils.isEmpty(customer.getMobile())){
            throw  new BusinessException("手机号不能为空");
        }
        Example example = new Example(Customer.class);
        example.createCriteria().andEqualTo("mobile",customer.getMobile())
                                .andEqualTo("projectId",customer.getProjectId())
                                 .andNotEqualTo("id",customer.getId());
        List<Customer> customers = customerMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(customers)){
            throw new BusinessException("【手机号已存在】该客户信息已经被录入");
        }
        this.updateSelective(customer);
    }


    @Override
    public PageInfo<Customer> selectMyselfCustomer(String keyWord,Long projectId,Long userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Example example = new Example(Customer.class);
        example.setOrderByClause("coming_time desc");
        example.createCriteria().andEqualTo("userId", userId)
                                .andEqualTo("projectId",projectId);
        if (StringUtils.isNotEmpty(keyWord)){
            Example.Criteria criteria = example.createCriteria().orLike("name", "%"+keyWord+"%")
                    .orLike("mobile", "%"+keyWord+"%");
            example.and(criteria);
        }
        List<Customer> customerList = customerMapper.selectByExample(example);
        return  new PageInfo<>(customerList);
    }

    @Override
    public List<Customer> selectCustomerByMobile(String mobile, Long projectId) {
        Example example = new Example(Customer.class);
        example.createCriteria().andEqualTo("mobile",mobile)
                .andEqualTo("projectId",projectId);
        List<Customer> list = customerMapper.selectByExample(example);
        return list;
    }


    @Override
    public Customer selectCusomerInfo(Long CustomerId) {
        Customer customer = customerMapper.selectByPrimaryKey(CustomerId);
        return customer;
    }

    @Override
    public void deleteBatch(List<Long> customerIds) {
        Example example = new Example(Customer.class);
        example.createCriteria().andIn("id",customerIds);
        customerMapper.deleteByExample(example);
    }

    @Override
    public PageInfo<Customer> selectAllCustomer(CustomerDTO customerDTO) {
        if (customerDTO.getPage()!=null&&customerDTO.getPageSize()!=null){
            PageHelper.startPage(customerDTO.getPage(),customerDTO.getPageSize());
        }
        List<Customer> list = customerMapper.selctList(customerDTO);
        return new PageInfo<>(list);
    }

    @Override
    public int countToday(Long projectId) {
        Date date = new Date();
        Date beginDate = DateUtils.getBeginDate(date);
        Date endDate = DateUtils.getEndDate(date);
        Example example = new Example(Customer.class);
        example.createCriteria().
                andEqualTo("projectId",projectId).
                andBetween("comingTime",beginDate,endDate);
        int i = customerMapper.selectCountByExample(example);
        return i;
    }

    @Override
    public Map<String, Object> importDatas(List<CustomerImport> list,Long projectId) {
        HashMap<String, Object> map = Maps.newHashMap();
        List<Customer> customerArrayList = Lists.newArrayList();//需要新增的集合
        List<CustomerImport> repeatCustomerList=Lists.newArrayList();//重复的集合
        List<SysUser> sysUsers = sysUserDao.selectAll();//查出所有的用户
        HashMap<String, Object> sysUsersMap = Maps.newHashMap();
        sysUsers.forEach(e->{
            sysUsersMap.put(e.getUsername(),e.getUserId());
        });
        List<String> mobileByProject = customerMapper.selectMobileByProject(projectId);
        long start = System.currentTimeMillis();
        for (CustomerImport customerImport : list) {
            String mobile = customerImport.getMobile();
            if (StringUtils.isEmpty(mobile)||mobile.length()>20||StringUtils.isEmpty(customerImport.getName())){
                continue;
            }
            if (mobileByProject.contains(mobile)){
                repeatCustomerList.add(customerImport);
            }else {
                Customer customer = new Customer();
                BeanUtils.copyProperties(customerImport,customer);
                SourceWayEnum byMessage = EnumUtil.getByMessage(customerImport.getSourceWay(), SourceWayEnum.class);
                if (byMessage!=null){
                    customer.setSourceWay(byMessage.getCode());
                }
                customer.setProjectId(projectId);
                customer.setCreateTime(new Date());
                customer.setUpdateTime(new Date());
                Object object = sysUsersMap.get(customer.getUserName());
                if (object!=null){
                    customer.setUserId((Long)object);
                }
                customerArrayList.add(customer);
            }

        }
        int insertNew=0;
        if (CollectionUtils.isNotEmpty(customerArrayList)){
            insertNew= customerMapper.insertList(customerArrayList);
        }
        long time = (System.currentTimeMillis() - start) / 1000;
        System.out.println("耗时："+time);
        map.put("insertNew",insertNew);
        map.put("repeat",repeatCustomerList.size());
        return map;
    }

    @Override
    public void updateCustomerUser(Long customerId, Long userId) {
        Customer customer = this.queryById(customerId);
        if (customer==null){
            throw new BusinessException("此客户不存在");
        }
        customer.setUserId(userId);
        customerMapper.updateByPrimaryKey(customer);
    }

    @Override
    public void updateUserBatch(UserBatchDTO userBatchDTO) {
        if (CollectionUtils.isEmpty(userBatchDTO.getIds())){
            return;
        }
        if (userBatchDTO.getIds()==null){
            throw new BusinessException("请选择置业顾问");
        }
        Example example = new Example(Customer.class);
        example.createCriteria().andIn("id",userBatchDTO.getIds());
        Customer customer = new Customer();
        customer.setUserId(userBatchDTO.getUserId());
        customerMapper.updateByExampleSelective(customer,example);
    }


    public List<Customer> selectAllCustomerByProjectId(Long projectId){
        Example example = new Example(Customer.class);
        example.createCriteria().
                andEqualTo("projectId",projectId);
        List<Customer> list = customerMapper.selectByExample(example);
        return list;
    }

    @Override
    public void removeUserId(List<Long> customerIdList) {
        customerMapper.removeUserId(customerIdList);
    }
}
