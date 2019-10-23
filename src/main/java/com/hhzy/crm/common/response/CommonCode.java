package com.hhzy.crm.common.response;

import lombok.ToString;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 18:33.
 * @Modified By:
 */

@ToString
public enum CommonCode implements ResultCode{
    INVALID_PARAM(10003,"非法参数！"),
    FAIL(11111,"操作失败！"),
    UNAUTHENTICATED(10001,"此操作需要登陆系统！"),
    UNAUTHORISE(10002,"权限不足，无权操作！"),
    SERVER_ERROR(99999,"抱歉，系统繁忙，请稍后重试！");


    //操作代码
    int code;
    //提示信息
    String message;

    private CommonCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


}
