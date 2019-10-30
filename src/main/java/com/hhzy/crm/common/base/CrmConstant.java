package com.hhzy.crm.common.base;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 17:18
 * @Description:
 */
public class CrmConstant {
    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;


    public static final String phoneRegex= "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

    public class Permissions{

        public static final String SENSITIVE="sensitive";

        public static final String UPDATEUSER="updateuser";
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
