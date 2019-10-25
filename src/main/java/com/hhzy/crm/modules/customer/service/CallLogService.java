package com.hhzy.crm.modules.customer.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.modules.customer.dataobject.dto.CallLogDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.CallLog;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/10 20:10
 * @Description:
 */
public interface CallLogService {

    void saveCallLog(CallLog callLog);

    void updateCallLog(CallLog callLog);

    CallLog  selectCallLogById(Long id);

    PageInfo<CallLog> selectList(CallLogDTO callLogDTO);

    void deleteBatch(List<Long> callLogIdList);

    void updateUser(Long id,Long userId);

    void updateUserBatch(UserBatchDTO userBatchDTO);


    /**
     * 小程序删除客户 把 置业顾问变为null
     */

    void removeUserId(List<Long> callIdList);

    List<CallLog> selectByMobile(Long projectId,String mobile);
}
