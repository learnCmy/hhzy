package com.hhzy.crm.common.enums;

import lombok.Getter;

/**
 * @Auther: cmy
 * @Date: 2019/8/10 22:47
 * @Description:
 */
@Getter
public enum HouseStatusEnum implements CodeEnum {

    UNSELLING(0,"未售"),
    SELLING(1, "在售"),
    FINISH(2, "已售");

    private Integer code;

    private String message;

    HouseStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
