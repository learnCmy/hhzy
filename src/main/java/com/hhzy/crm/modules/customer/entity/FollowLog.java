package com.hhzy.crm.modules.customer.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "follow_log")
@Data
public class FollowLog {

    @Column(name = "project_id")
    private Long projectId;

    @Id
    private Long id;

    /**
     * 联系方式
     */
    @Column(name = "contact_Way")
    private String contactWay;

    @Column(name = "name")
    private String name;


    @Column(name = "mobile")
    private String mobile;

    /**
     * 主题
     */
    private String subject;

    /**
     * 联系详情
     */
    @Column(name = "contact_detail")
    private String contactDetail;

    /**
     * 下次回访日期
     */
    @Column(name = "next_visit_time")
    private Date nextVisitTime;

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


    @Transient
    private String userName;


}