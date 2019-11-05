package com.hhzy.crm.modules.customer.entity;

import lombok.Data;

import javax.persistence.*;

@Data
public class Project {
    @Id
    private Long id;

    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 居住地址 用逗号分隔 比如 xx路,yy路
     */
    @Column(name = "live_address_config")
    private String liveAddressConfig;

    /**
     * 工作地址 用逗号分隔 比如 xx路，yy路
     */
    @Column(name = "work_address_config")
    private String workAddressConfig;


    @Column(name = "bank_category_config")
    private String bankCategoryConfig;

}