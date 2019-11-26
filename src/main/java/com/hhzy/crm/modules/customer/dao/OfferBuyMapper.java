package com.hhzy.crm.modules.customer.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.OfferBuyDTO;
import com.hhzy.crm.modules.customer.dataobject.vo.Top10OfferBuyVo;
import com.hhzy.crm.modules.customer.entity.OfferBuy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OfferBuyMapper extends MyMapper<OfferBuy> {

    List<OfferBuy> selectByName(@Param("keyWord") String keyWord, @Param("userId") Long userId, @Param("projectId") Long projectId);


    List<OfferBuy> selectList(@Param("offerBuyDTO")OfferBuyDTO offerBuyDTO);

    List<Top10OfferBuyVo> selectTop10(@Param("projectId")Long projectId);

    /**
     * 小程序删除客户 把 置业顾问变为null
     */

    Integer removeUserId(@Param("ids") List<Long> ids);

    int countNumberByStatus(@Param("projectId") Long projectId,@Param("status") Integer status);


    int countSignNumber(@Param("projectId") Long projectId);

    int countLoanNumber(@Param("projectId") Long projectId);

}