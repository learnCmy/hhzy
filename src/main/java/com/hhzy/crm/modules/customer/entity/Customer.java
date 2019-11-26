package com.hhzy.crm.modules.customer.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(value = "来访客户信息表")
public class Customer {

    @Id
    private Long id;

    @Column(name = "project_id")
    @NotNull(message = "项目id不能为空")
    private Long projectId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "客户姓名")
    @NotEmpty(message = "客户姓名不能为空")
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    /**
     * 来访日期
     */
    @Column(name = "coming_time")
    private Date comingTime;

    /**
     * 年龄
     */
    private String age;

    /**
     * 居住地址
     */
    @ApiModelProperty("居住地址")
    private String address;

    /**
     * 职业类型
     */
    @ApiModelProperty("职业类型")
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

    @Transient
    private String  sourceWayStr;

    /**
     * 产品需求
     */
    @Column(name = "product_requirement")
    @ApiModelProperty("产品需求")
    private String productRequirement;

    /**
     * 面积需求
     */
    @Column(name = "acreage_requirement")
    @ApiModelProperty("面积需求")
    private String acreageRequirement;

    /**
     * 意向楼层
     */
    @Column(name = "purpose_floor")
    @ApiModelProperty("意向楼层")
    private String purposeFloor;

    /**
     * 意向价格
     */
    @Column(name = "purpose_price")
    private String purposePrice;

    /**
     * 客户意向
     */
    @ApiModelProperty("客户意向")
    private String purpose;

    /**
     * 付款方式
     */
    @Column(name = "pay_way")
    private String payWay;

    /**
     * 关注点
     */
    @Column(name = "focus_point")
    private String focusPoint;

    /**
     * 抗拒点
     */
    @Column(name = "resist_point")
    private String resistPoint;


    /**
     * 备注
     */
    private String remark;

    /**
     * 置业顾问Id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 置业 动机
     */
    @Column(name = "buy_motive")
    private String buyMotive;


    /**
     * 置业顾问名字
     */
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

    @Column(name = "delete_by")
    private Long deleteBy;

    @Transient
    private Long followId;
}