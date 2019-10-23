package com.hhzy.crm.modules.customer.dataobject.importPOI;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/10/14 11:13
 * @Description:
 */
@Data
public class IdentifyImport {

    /**
     * 客户姓名
     */
    @Excel(name = "客户姓名")
    private  String name;


    @Excel(name="手机号码")
    private String mobile;

    @Excel(name = "年龄")
    private String age;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号码")
    private String  idCard;

    /**
     * 身份证地址
     */
    @Excel(name = "身份证地址")
    private String idAddress;


    /**
     * 认筹金
     */
    @Excel(name = "认筹金")
    private BigDecimal identifyPrice;

    @Excel(name = "备注")
    private String remark;

    @Excel(name = "置业顾问")
    private String userName;

    /**
     * 买售状态 （1-已购买商铺 2已购买住宅 3- 已退卡）
     */
    @Excel(name = "买售状态")
    private String sellStatusStr;

    /**
     * 退卡日期
     */
    @Excel(name = "退卡日期",format = "yyyy-MM-dd")
    private Date refundTime;

    /**
     * 认筹日期
     */
    @Excel(name = "日期",format = "yyyy-MM-dd")
    private Date identifyTime;

    /**
     * 职业
     */
    @Excel(name = "职业")
    private String profession;

    /**
     * 产品需求
     */
    @Excel(name = "产品需求")
    private String productRequirement;

    /**
     * 购买力评估
     */
    @Excel(name = "购买力评估")
    private String purpose;

    /**
     * 意向价格
     */
    @Excel(name = "意向价格")
    private String purposePrice;

    /**
     * 认筹途径
     */
    @Excel(name = "认筹途径")
    private String sourceWayStr;

    /**
     * 置业动机
     */
    @Excel(name = "置业动机")
    private String  buyMotive;


}
