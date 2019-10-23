package com.hhzy.crm.common.utils;


import com.hhzy.crm.common.enums.CodeEnum;

/**
 * Created by 廖师兄
 * 2017-07-16 18:36
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        if (code==null){
            return null;
        }
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }


    public static <T extends CodeEnum> T getByMessage(String message, Class<T> enumClass) {
        if (message==null){
            return null;
        }
        for (T each: enumClass.getEnumConstants()) {
            if (message.equals(each.getMessage())) {
                return each;
            }
        }
        return null;
    }

}
