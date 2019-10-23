package com.hhzy.crm.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Auther: cmy
 * @Date: 2019/8/4 13:10
 * @Description:
 */
public class KeyUtils {

    /**
     * 客户编号生成器：时间+随机数
     * @return
     */
    public static synchronized  String genUniqueKey(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
        String key = sdf.format(new Date());
        String strKey = "KH:" + key;
        Random random=new Random();
        int number = random.nextInt(100);
        return strKey+number;
    }


}
