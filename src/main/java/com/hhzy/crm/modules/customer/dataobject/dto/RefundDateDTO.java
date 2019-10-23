package com.hhzy.crm.modules.customer.dataobject.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/10/21 15:36
 * @Description:
 */
@Data
public class RefundDateDTO {

    private Long projectId;

    private Long id;

    private Date refundDate;

}
