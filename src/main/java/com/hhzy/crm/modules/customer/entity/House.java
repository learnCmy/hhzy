package com.hhzy.crm.modules.customer.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class House {



    @Id
    private Long id;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Long projectId;

    /**
     * 房源名称
     */
    private String name;

    /**
     * 商铺信息
     */
    @Column(name = "house_info")
    private String houseInfo;

    /**
     * 建筑面积
     */
    private BigDecimal acreage;

    /**
     * 建面单价
     */
    @Column(name = "acreage_price")
    private BigDecimal acreagePrice;

    /**
     * 商铺总价
     */
    @Column(name = "amount_price")
    private BigDecimal amountPrice;

    /**
     * 认购信息表Id
     */
    @Column(name = "offer_buy_id")
    private Long offerBuyId;

    /**
     * 签约信息Id
     */
    @Column(name = "sign_id")
    private Long signId;

    /**
     * 0-未售",1-‘在售’，2-‘已售’
     */
    private Integer status;

    @Transient
    private String statusStr;

    /**
     * 房源描述
     */
    private String info;

    /**
     * 房源类别 1- 住宅 2-商铺
     */
    private Integer type;

    @Transient
    private String typeStr;

    /**
     * 楼层
     */
    @Column(name = "floor_level")
    private String floorLevel;

    /**
     * 几幢
     */
    @Column(name = "build_no")
    private String buildNo;

    /**
     * 房号
     */
    @Column(name = "room_no")
    private String roomNo;

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


    private String remark;


}