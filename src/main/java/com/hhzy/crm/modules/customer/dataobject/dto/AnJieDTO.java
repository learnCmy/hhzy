package com.hhzy.crm.modules.customer.dataobject.dto;

import lombok.Data;

/**
 * @Auther: cmy
 * @Date: 2019/8/24 10:33
 * @Description:
 */
@Data
public class AnJieDTO {

    private Long userId;

    /**
     * 贷款金额
     */
    private String loansPrivce;

    /**
     * 是否交件
     */
    private Boolean isSubmit;

    /**
     * 是否网签
     */
    private Boolean isNetSign;


}
