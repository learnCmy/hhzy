package com.hhzy.crm;

import cn.afterturn.easypoi.util.PoiPublicUtil;
import com.hhzy.crm.modules.customer.entity.Customer;
import org.springframework.beans.BeanUtils;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

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
        String regex = "；|;|\\s+";
        String s1="411488986@qq.com；cmy@olymtech.com";
        String[] split = s1.split(regex);
        for (String s2 : split) {
            System.out.println(s2);
        }
        long l = TimeUnit.HOURS.toSeconds(2);
        System.out.println(l);
        Customer customer = new Customer();
        customer.setSex(12314);
        customer.setRemark("hahah");
        Customer customer1 = new Customer();
        BeanUtils.copyProperties(customer,customer1);
        customer1.setRemark("caocaocao");
        System.out.println(customer.getRemark());
    }
}
