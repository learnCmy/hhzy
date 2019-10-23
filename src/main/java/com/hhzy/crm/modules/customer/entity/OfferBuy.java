package com.hhzy.crm.modules.customer.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "offer_buy")
@Data
public class OfferBuy {
    @Id
    private Long id;

    @Column(name = "project_id")
    private Long projectId;
    /**
     * 认购者信息
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 身份证
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 通讯地址
     */
    private String address;

    /**
     * 认购日期
     */
    @Column(name = "offer_buy_time")
    private Date offerBuyTime;

    /**
     * 职业顾问Id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 定金
     */
    @Column(name = "pre_price")
    private BigDecimal prePrice;

    /**
     * 房屋Id
     */
    @Column(name = "house_id")
    private Long houseId;


    @Transient
    private String houseName;


    @Transient
    private String userName;

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

    /**
     * 审核状态（0未审核 1 审核通过 2 审核驳回)
     */
    private Integer status;

    @Transient
    private Long  signId;

    /**
     * 工作区域
     */
    @Column(name = "work_address")
    private String workAddress;

    private String age;

    /**
     * 职业类型
     */
    private String profession;

    /**
     * 家庭结构
     */
    @Column(name = "home_structure")
    private String homeStructure;

    /**
     * 客户来源
     */
    @Column(name = "source_way")
    private Integer sourceWay;


    /**
     * 置业 动机
     */
    @Column(name = "buy_motive")
    private String buyMotive;

}