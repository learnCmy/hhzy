package com.hhzy.crm.modules.customer.entity;

import lombok.Data;

import javax.persistence.*;

@Data
public class Tookeen {
    @Id
    private Long id;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Long projectId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 职业
     */
    private String profession;

    /**
     * 拓客区域
     */
    @Column(name = "tookeen_address")
    private String tookeenAddress;

    /**
     * 意向产品
     */
    @Column(name = "purpose_pro")
    private String purposePro;

    /**
     * 备注
     */
    private String remark;

    /**
     * 置业顾问Id
     */
    @Column(name = "user_id")
    private Long userId;


}