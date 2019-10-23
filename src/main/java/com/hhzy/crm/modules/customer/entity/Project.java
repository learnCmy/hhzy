package com.hhzy.crm.modules.customer.entity;

import javax.persistence.*;

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

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取项目名称
     *
     * @return project_name - 项目名称
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * 设置项目名称
     *
     * @param projectName 项目名称
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 获取居住地址 用逗号分隔 比如 xx路,yy路
     *
     * @return live_address_config - 居住地址 用逗号分隔 比如 xx路,yy路
     */
    public String getLiveAddressConfig() {
        return liveAddressConfig;
    }

    /**
     * 设置居住地址 用逗号分隔 比如 xx路,yy路
     *
     * @param liveAddressConfig 居住地址 用逗号分隔 比如 xx路,yy路
     */
    public void setLiveAddressConfig(String liveAddressConfig) {
        this.liveAddressConfig = liveAddressConfig;
    }

    /**
     * 获取工作地址 用逗号分隔 比如 xx路，yy路
     *
     * @return work_address_config - 工作地址 用逗号分隔 比如 xx路，yy路
     */
    public String getWorkAddressConfig() {
        return workAddressConfig;
    }

    /**
     * 设置工作地址 用逗号分隔 比如 xx路，yy路
     *
     * @param workAddressConfig 工作地址 用逗号分隔 比如 xx路，yy路
     */
    public void setWorkAddressConfig(String workAddressConfig) {
        this.workAddressConfig = workAddressConfig;
    }
}