package com.hhzy.crm;

import cn.afterturn.easypoi.util.PoiPublicUtil;
import org.joda.time.DateTime;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
       String date="2019-8-18";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = dateFormat.parse(date);
        System.out.println(parse);
        String s = new DateTime(parse).toString("yyyy-MM-dd");
        System.out.println(s);

    }
}
