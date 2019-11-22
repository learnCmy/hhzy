package com.hhzy.crm.common.utils;

import com.hhzy.crm.common.base.CrmConstant;
import com.hhzy.crm.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @Auther: cmy
 * @Date: 2019/11/17 19:53
 * @Description:
 */
public class MobileUtils {

    public static String  getMobile(String mobile){
        if (StringUtils.isEmpty(mobile)){
            throw  new BusinessException("手机号不能为空");
        }
        String trimMobile = mobile.replaceAll(" ", "");
        if (trimMobile.length()>11){
            throw new BusinessException("手机号格式过长 请不要有其他符号【空格下划线请去除】");
        }
        Pattern pattern = Pattern.compile(CrmConstant.numberRegex);
        boolean matches = pattern.matcher(trimMobile).matches();
        if (matches){
            return trimMobile;
        }else {
            throw new BusinessException("输入的手机号不合法 存在非数字");
        }

    }
}
