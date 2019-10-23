package com.hhzy.crm.modules.customer.dataobject.vo;

import com.hhzy.crm.modules.customer.entity.SignInfo;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/9/21 13:25
 * @Description:
 */
@Data
public class SignVo extends SignInfo {

    private Long houseId;

    /**
     * 房源名称
     */
    private String houseName;

    /**
     * 商铺信息
     */
    private String houseInfo;

    /**
     * 建筑面积
     */
    private BigDecimal acreage;

    /**
     * 建面单价
     */
    private BigDecimal acreagePrice;

    /**
     * 商铺总价
     */
    private BigDecimal amountPrice;

    /**
     * 0-未售",1-‘在售’，2-‘已售’
     */
    private Integer houseStatus;

    private String houseStatusStr;

    private String statusHouse;

    /**
     * 房源描述
     */
    private String houseDescribe;

    /**
     * 房源类别 1- 住宅 2-商铺
     */
    private Integer houseType;

    /**
     * 楼层
     */
    private String floorLevel;

    /**
     * 几幢
     */
    private String buildNo;

    /**
     * 房号
     */
    private String roomNo;


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
    private String idCard;

    /**
     * 通讯地址
     */
    private String address;

    /**
     * 认购日期
     */
    private Date offerBuyTime;

    /**
     * 职业顾问Id
     */
    private Long userId;

    /**
     * 定金
     */
    private BigDecimal prePrice;

    /**
     * 置业顾问
     */
    private String userName;


    /**
     * 审核状态（0未审核 1 审核通过 2 审核驳回)
     */
    private Integer offerStatus;

    @Transient
    private Boolean isSign;

    /**
     * 工作区域
     */
    private String workAddress;

    private String age;

    /**
     * 职业类型
     */
    private String profession;

    /**
     * 家庭结构
     */
    private String homeStructure;

    /**
     * 客户来源
     */
    private Integer sourceWay;

    private String sourceWayStr;


    /**
     * 置业 动机
     */
    private String buyMotive;
}
