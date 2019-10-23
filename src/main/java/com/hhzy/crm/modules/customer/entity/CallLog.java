package com.hhzy.crm.modules.customer.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "call_log")
@Data
public class CallLog {
    @Id
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    /**
     * 来电日期
     */
    @Column(name = "call_time")
    private Date callTime;


    /**
     * 客户姓名
     */
    @Column(name = "name")
    private  String name;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "mobile")
    private String mobile;


    /**
     * 获取途径
     */
    @Column(name = "acquiring_way")
    private Integer acquiringWay;

    @Transient
    private String acquiringWayStr;

    /**
     * 询问内容1
     */
    @Column(name = "ask_content_one")
    private String askContentOne;

    /**
     * 询问内容2
     */
    @Column(name = "ask_content_two")
    private String askContentTwo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 职业顾问Id
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



    @Transient
    private String userName;

}