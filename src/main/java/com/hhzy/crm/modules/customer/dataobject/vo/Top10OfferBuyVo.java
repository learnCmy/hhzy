package com.hhzy.crm.modules.customer.dataobject.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: cmy
 * @Date: 2019/9/28 13:01
 * @Description:
 */
@Data
public class Top10OfferBuyVo {

    private String username;

    private Integer num;

    private BigDecimal amountPrice;


    private Integer userId;


}
