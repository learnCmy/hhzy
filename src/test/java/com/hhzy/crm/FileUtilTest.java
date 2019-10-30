package com.hhzy.crm;

import cn.afterturn.easypoi.util.PoiPublicUtil;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtilTest {

    public static String getWebRootPath(String filePath) {
        try {
            String path = PoiPublicUtil.class.getClassLoader().getResource("").toURI().getPath();
            path = path.replace("WEB-INF/classes/", "");
            path = path.replace("file:/", "");
            return path + filePath;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ParseException {
       /*String date="2019-8-18";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = dateFormat.parse(date);
        System.out.println(parse);
        String s = new DateTime(parse).toString("yyyy-MM-dd");
        System.out.println(s);*/

        String phone="18724605668";
        String regex = "^((1[0-9][0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        System.out.println(isMatch);

      /*  SignVo signVo = new SignVo();
        String aa =true==signVo.getIsSubmit()?"是":"否";
        System.out.println(aa);*/


    }
}
