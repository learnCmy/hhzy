package com.hhzy.crm.modules.customer.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.customer.dataobject.dto.IdentifyDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.dataobject.importPOI.IdentifyImport;
import com.hhzy.crm.modules.customer.entity.IdentifyLog;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 10:04
 * @Description:
 */
public interface IdentifyService extends BaseService<IdentifyLog> {


    PageInfo<IdentifyLog> selcetByKeyWord(String keyWord,Long projectId,Long userId,Integer page,Integer pageSize);

    IdentifyLog selectById(Long Id);

    void deleteBatch(List<Long> identifyIdList);

    PageInfo<IdentifyLog> selectList(IdentifyDTO identifyDTO);

    void  updateUser(Long id,Long UserId);


    void updateUserBatch(UserBatchDTO userBatchDTO);

    /**
     * 小程序删除客户 把 置业顾问变为null
     */

    void removeUserId(List<Long> ids);


    void updateSellStatus(Long id, Integer sellStatus);

    List<IdentifyLog> selectByMobile(Long projectId,String mobile);

    Map<String,Object> importDatas(List<IdentifyImport> list, Long projectId);

    void updateByPrimaryKeySelective(IdentifyLog identifyLog);


    Map<String,Object> countNumberByStatus(Long projectId);
}
