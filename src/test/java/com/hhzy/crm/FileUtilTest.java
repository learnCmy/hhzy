package com.hhzy.crm;

import cn.afterturn.easypoi.util.PoiPublicUtil;

import java.net.URISyntaxException;
import java.text.ParseException;

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

        /*String phone="1872460566 8";
        String trim = phone.trim();
        trim.replace(" ", "");
        System.out.println(trim);
        System.out.println(trim.length());
        String regex = "^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        System.out.println(isMatch);*/

      /*  SignVo signVo = new SignVo();
        String aa =true==signVo.getIsSubmit()?"是":"否";
        System.out.println(aa);*/
        System.out.println("aa".equals(null));

    }
}
