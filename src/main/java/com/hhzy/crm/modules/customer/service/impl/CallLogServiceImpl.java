package com.hhzy.crm.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.exception.BusinessException;
import com.hhzy.crm.modules.customer.dao.CallLogMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.CallLogDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.CallLog;
import com.hhzy.crm.modules.customer.entity.Customer;
import com.hhzy.crm.modules.customer.service.CallLogService;
import com.hhzy.crm.modules.customer.service.CustomerService;
import com.hhzy.crm.modules.sys.service.SysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/10 20:11
 * @Description:
 */
@Service
public class CallLogServiceImpl implements CallLogService {

    @Autowired
    private CallLogMapper callLogMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SysUserService sysUserService;

    public void saveCallLog(CallLog callLog){
        saveCheck(callLog.getMobile(),callLog.getProjectId(),null);
        callLog.setUpdateTime(new Date());
        callLog.setCreateTime(new Date());
        callLogMapper.insertSelective(callLog);
    }

    @Override
    public void updateCallLog(CallLog callLog) {
        this.saveCheck(callLog.getMobile(),callLog.getProjectId(),callLog.getId());
        callLog.setUpdateTime(new Date());
        callLogMapper.updateByPrimaryKeySelective(callLog);
    }


    /**
     * 保存更新前 数据检查
     * @param mobile
     * @param projectId
     * @param callLogId
     */
    private void saveCheck(String mobile,Long projectId,Long callLogId){
        List<Customer> list = customerService.selectCustomerByMobile(mobile,projectId);
        if (CollectionUtils.isNotEmpty(list)){
            String userName = sysUserService.getUserName(list.get(0).getUserId());
            throw new BusinessException("【已有来访记录】该客户信息已被【"+userName+"】录入");
        }
        Example example = new Example(CallLog.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("mobile", mobile)
                .andEqualTo("projectId", projectId);
        if (callLogId!=null){
            criteria.andNotEqualTo("id",callLogId);
        }
        List<CallLog> callLogs = callLogMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(callLogs)){
            String userName = sysUserService.getUserName(list.get(0).getUserId());
            throw  new BusinessException("【已有来电记录】该客户信息已被【"+userName+"】录入");
        }
    }



    @Override
    public CallLog selectCallLogById(Long id) {
        CallLog callLog = callLogMapper.selectByPrimaryKey(id);
        if (callLog==null){
            throw new BusinessException("所选来电记录不存在");
        }
        return callLog;
    }

    @Override
    public PageInfo<CallLog> selectList(CallLogDTO callLogDTO) {
        if (callLogDTO.getPage()!=null&&callLogDTO.getPageSize()!=null){
            PageHelper.startPage(callLogDTO.getPage(),callLogDTO.getPageSize());
        }
        List<CallLog> list = callLogMapper.list(callLogDTO);
        return new PageInfo<>(list);
    }

    @Override
    public void deleteBatch(List<Long> callLogIdList) {
        Example example = new Example(CallLog.class);
        example.createCriteria().andIn("id",callLogIdList);
        callLogMapper.deleteByExample(example);

    }

    @Override
    public void updateUser(Long id, Long userId) {
        CallLog callLog = this.selectCallLogById(id);
        callLog.setUserId(userId);
        this.updateCallLog(callLog);
    }

    @Override
    public void updateUserBatch(UserBatchDTO userBatchDTO) {
        if (CollectionUtils.isEmpty(userBatchDTO.getIds())){
            return;
        }
        if (userBatchDTO.getIds()==null){
            throw new BusinessException("请选择置业顾问");
        }
        Example example = new Example(CallLog.class);
        example.createCriteria().andIn("id",userBatchDTO.getIds());
        CallLog callLog = new CallLog();
        callLog.setUserId(userBatchDTO.getUserId());
        callLogMapper.updateByExampleSelective(callLog,example);
    }

    @Override
    public void removeUserId(List<Long> callIdList) {
        callLogMapper.removeUserId(callIdList);
    }

    @Override
    public List<CallLog> selectByMobile(Long projectId, String mobile) {
        Example example = new Example(CallLog.class);
        example.createCriteria().andEqualTo("mobile",mobile)
                                .andEqualTo("projectId",projectId);
        List<CallLog> callLogs = callLogMapper.selectByExample(example);
        return callLogs;
    }

}
