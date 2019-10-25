package com.hhzy.crm.modules.customer.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.customer.dataobject.dto.OfferBuyDTO;
import com.hhzy.crm.modules.customer.dataobject.dto.UserBatchDTO;
import com.hhzy.crm.modules.customer.entity.OfferBuy;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 10:14
 * @Description:
 */
public interface OfferBuyService extends BaseService<OfferBuy> {

    OfferBuy selectById(Long id);

    void  deleteBatch(List<Long> ids);

    void deleteBatchForWeb(List<Long> ids);

    PageInfo<OfferBuy> selectByName(String keyWord,Long userId,Long projectId, Integer page, Integer pageSize);

    void updateOfferBuy(OfferBuy offerBuy);

    void updatePrePrice(OfferBuy offerBuy);


    PageInfo<OfferBuy> selectList(OfferBuyDTO offerBuyDTO);

    void updateUser(Long id,Long userId);

    void updateStatus(Long id,Integer status);

    void updateUserBatch(UserBatchDTO userBatchDTO);

    void removeUserId(List<Long> ids);

    List<OfferBuy>  selectByMobile(Long projectId,String mobile);


    Map<String,Object> countNumberByStatus(Long projectId);
}
