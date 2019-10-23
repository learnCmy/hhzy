package com.hhzy.crm.common.enums;

import lombok.Getter;

/**
 * @Auther: cmy
 * @Date: 2019/10/14 11:08
 * @Description:
 */
@Getter
public enum  IdentifySellStatusEnum implements CodeEnum {
    BUYCARD(0,"已购卡"),
    BUYSHANGPU(1,"已购买商铺"),
    BUYZHUZHAI(2,"已购买住宅"),
    REFUNDCARD(3,"已退卡");

    private Integer code;

    private String message;

    IdentifySellStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
