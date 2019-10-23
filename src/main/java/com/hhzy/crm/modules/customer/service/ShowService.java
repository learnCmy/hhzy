package com.hhzy.crm.modules.customer.service;

import com.hhzy.crm.modules.customer.dataobject.dto.ShowDTO;
import com.hhzy.crm.modules.customer.dataobject.vo.Top10CusomterVO;
import com.hhzy.crm.modules.customer.dataobject.vo.Top10OfferBuyVo;

import java.util.List;
import java.util.Map;

/**
 * @Auther: cmy
 * @Date: 2019/9/24 22:18
 * @Description:
 */
public interface ShowService {

    Map<String,Object> count(ShowDTO showDTO);

    Map<String,Object> coutSell(Long projectId);

    List<Top10CusomterVO> listTop10Customer(Long projectId);

    List<Top10OfferBuyVo> listTop10OfferBuy(Long projectId);

    Map<String,Object> lineChart(Long projectId);
}
