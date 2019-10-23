package com.hhzy.crm.modules.customer.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.modules.customer.dataobject.dto.FollowLogDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.FollowLog;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/11 13:06
 * @Description:
 */
public interface FollowLogService {

    void  save(FollowLog followLog);

    void  update(FollowLog followLog);

    PageInfo<FollowLog> selcetByKeyWord(String keyWord,Long project,Long userId,Integer page,Integer pageSize);

    FollowLog selectById(Long id);

    void  deleteBatch(List<Long>  followLogIdList);

    void updateUser(Long id,Long userId);

    FollowLog selectByMobile(Long projectId,String mobile);

    PageInfo<FollowLog> select(FollowLogDTO followLogDTO);


    /**
     * 小程序删除客户 把 置业顾问变为null
     */

    void removeUserId(List<Long> ids);

    void updateUserBatch(UserBatchDTO userBatchDTO);

}
