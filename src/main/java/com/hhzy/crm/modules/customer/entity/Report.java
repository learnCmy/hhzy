package com.hhzy.crm.modules.customer.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
public class Report {

    @Id
    private Long id;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Long projectId;

    /**
     * 年龄
     */
    private String age;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别(1 是 男 2是女）
     */
    private Integer sex;

    @Transient
    private String sexStr;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 产品类型  1- 住宅 2-商铺
     */
    @Column(name = "product_type")
    private Integer productType;

    @Transient
    private String productTypeStr;

    /**
     * 途径
     */
    @Column(name = "source_way")
    private String sourceWay;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 面积
     */
    private String acreage;

    /**
     * 预计到访时间
     */
    @Column(name = "plan_time")
    private Date planTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 介绍人
     */
    private String introducer;

    /**
     * 置业顾问Id
     */
    @Column(name = "user_id")
    private Long userId;

    @Transient
    private String userName;

}