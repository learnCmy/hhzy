package com.hhzy.crm.modules.customer.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "identify_log")
@Data
public class IdentifyLog {

    @Column(name = "project_id")
    private Long projectId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 客户姓名
     */
    @Column(name = "name")
    private  String name;



    @Column(name = "mobile")
    private String mobile;

    /**
     * 身份证号
     */
    @Column(name = "id_card")
    private String  idCard;

    /**
     * 身份证地址
     */
    @Column(name = "id_address")
    private String idAddress;

    /**
     * 工作单位
     */
    @Column(name = "company")
    private String company;

    /**
     * 工作单位地址
     */
    @Column(name = "company_address")
    private String companyAddress;


    /**
     * 认筹金
     */
    @Column(name = "identify_price")
    private BigDecimal identifyPrice;

    @Column(name = "vip_card")
    private String vipCard;

    /**
     * 置业顾问Id
     */
    @Column(name = "user_id")
    private Long userId;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "remark")
    private String remark;

    @Transient
    private String userName;

    /**
     * 买售状态 （1-已购买 2已购买住宅 3- 已退卡）
     */
    @Column(name = "sell_status")
    private Integer sellStatus;

    /**
     * 退卡日期
     */
    @Column(name = "refund_time")
    private Date refundTime;

    /**
     * 认筹日期
     */
    @Column(name = "identify_time")
    private Date identifyTime;

    /**
     * 职业
     */
    @Column(name = "profession")
    private String profession;

    /**
     * 产品需求
     */
    @Column(name = "product_requirement")
    private String productRequirement;

    /**
     * 购买力评估
     */
    @Column(name = "purpose")
    private String purpose;

    /**
     * 意向价格
     */
    @Column(name = "purpose_price")
    private String purposePrice;

    /**
     * 认筹途径
     */
    @Column(name = "source_way")
    private Integer sourceWay;

    @Transient
    private String sourceWayStr;


    /**
     * 置业动机
     */
    @Column(name = "buy_motive")
    private String  buyMotive;

    @Column(name = "age")
    private String age;
}
