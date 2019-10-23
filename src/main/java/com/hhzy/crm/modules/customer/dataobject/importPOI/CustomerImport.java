package com.hhzy.crm.modules.customer.dataobject.importPOI;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerImport {

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String name;

    /**
     * 性别
     */
    @Excel(name = "性别",replace = {"男_1","女_2"})
    private Integer sex;

    /**
     * 联系方式
     */
    @Excel(name = "联系方式")
    private String mobile;

    /**
     * 来访日期
     */
    @Excel(name = "日期")
    private Date comingTime;

    /**
     * 年龄
     */
    @Excel(name = "年龄")
    private String age;

    /**
     * 居住地址
     */
    @Excel(name = "居住地址")
    private String address;

    /**
     * 职业类型
     */
    @Excel(name = "职业类型")
    private String profession;

    /**
     * 家庭结构 
     */
    @Excel(name = "家庭结构")
    private String homeStructure;

    /**
     * 客户来源
     */
    @Excel(name = "客户来源")
    private String sourceWay;

    /**
     * 产品需求
     */
    @Excel(name = "产品需求")
    private String productRequirement;

    /**
     * 面积需求
     */
    @Excel(name = "面积需求")
    private String acreageRequirement;

    /**
     * 意向楼层
     */
    @Excel(name = "意向楼层")
    private String purposeFloor;

    /**
     * 意向价格
     */
    @Excel(name = "意向价格")
    private String purposePrice;

    /**
     * 客户意向
     */
    @Excel(name = "客户意向")
    private String purpose;

    /**
     * 付款方式
     */
    @Excel(name = "付款方式")
    private String payWay;

    /**
     * 关注点
     */
    @Excel(name = "关注点")
    private String focusPoint;

    /**
     * 抗拒点
     */
    @Excel(name="抗拒点")
    private String resistPoint;


    /**
     * 备注
     */
    @Excel(name="备注")
    private String remark;

    /**
     * 置业顾问
     */
    @Excel(name = "置业顾问")
    private String userName;

    /**
     * 置业 动机
     */
    @Excel(name = "置业动机")
    private String buyMotive;

}