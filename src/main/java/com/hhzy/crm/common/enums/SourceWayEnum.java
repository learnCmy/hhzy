package com.hhzy.crm.common.enums;

import lombok.Getter;

/**
 * @Auther: cmy
 * @Date: 2019/8/11 12:33
 * @Description:
 */
@Getter
public enum  SourceWayEnum implements CodeEnum {


    /**
     * 1:自然上访，2:员工邀约，3:老客户介绍（）写老客户名字，4:路过，5:朋友介绍，6:广告媒体 7其他
     */
    NATURE(1,"自然上访"),
    INVITE(2,"员工邀约"),
    RECOMMEND(3,"老客户介绍"),
    PASSING(4,"路过"),
    FRIEND(5,"朋友介绍"),
    ADVERTISING(6,"广告媒体"),
    OTHER(7,"其他")
    ;


    private Integer code;

    private String message;

    SourceWayEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
