package com.hhzy.crm.modules.customer.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.customer.dataobject.dto.TookeenDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.Tookeen;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/10/21 17:42
 * @Description:
 */
public interface TookeenService extends BaseService<Tookeen> {


    void  saveBasicTookeen(Tookeen tookeen);

    void updateBasicTookeen(Tookeen tookeen);

    List<Tookeen> selectByMobile(Long projectId,String mobile);

    PageInfo<Tookeen> selectList(TookeenDTO tookeenDTO);

    void  removeUserId(List<Long> ids);

    void deleteBatch(List<Long> ids);

    void updateUser(Long id,Long userId);

    void updateUserBatch(UserBatchDTO userBatchDTO);

}
