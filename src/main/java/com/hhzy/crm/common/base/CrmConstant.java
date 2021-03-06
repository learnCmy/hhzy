package com.hhzy.crm.common.base;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 17:18
 * @Description:
 */
public class CrmConstant {
    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;


    public static final String phoneRegex= "^((13[0-9])|(16[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

    public static  final  String numberRegex="^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";

    public static class Permissions{

        public static final String SENSITIVE="sensitive";


        public static  final  String RESIDENCE="residence";

        public static final  String SHOP="shop";

        public static final String UPDATEUSER="updateuser";

        public static  final  String LOOKOTHER="lookother";

        public static final String MYCUSTOMER="mycustomer";
    }

    public static class ActionType{

        public static final String INSERT="1";

        public static final String UPDATE="2";

        public static  final  String DELETE="3";

        public  static  final String EXPORT="4";

        public static final String IMPORT="5";
    }


    public static class Client{

        public static final String WEB="1";

        public static final String APP ="2";
    }






    /**
     * 菜单类型
     *
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年11月15日 下午1:24:29
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }




}
