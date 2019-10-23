package com.hhzy.crm.common.enums;

import lombok.Getter;

/**
 * Created by 廖师兄
 * 2017-06-11 17:12
 */
@Getter
public enum CustomerStatusEnum implements CodeEnum {

    Call(0,"来电"),
    IDENTIFY(1, "已认筹"),
    SIGN(2, "已签约"),
    SELL(3, "已购买");

    private Integer code;

    private String message;

    CustomerStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
