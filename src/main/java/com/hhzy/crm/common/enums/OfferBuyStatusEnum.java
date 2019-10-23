package com.hhzy.crm.common.enums;

import lombok.Getter;

/**
 * @Auther: cmy
 * @Date: 2019/9/15 21:00
 * @Description:
 */
@Getter
public enum OfferBuyStatusEnum implements CodeEnum {
    UNCHECKED(0,"未审核"),
    CHECKED(1,"审核通过"),
    REJECT(2,"审核驳回")
    ;
    private Integer code;

    private String message;

    OfferBuyStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
