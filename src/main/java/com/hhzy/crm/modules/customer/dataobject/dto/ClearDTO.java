package com.hhzy.crm.modules.customer.dataobject.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/11/29 21:11
 * @Description:
 */
@Data
public class ClearDTO {

    /**
     * 签约的ID
     */
    private Long signId;

    private Long projectId;

    /**
     * 是否结佣
     */
    private  Boolean commission;


    /**
     * 结算情况
     */
    private  String clearInfo;

    /**
     * 交房日期
     */
    private Date deliveryTime;

}
