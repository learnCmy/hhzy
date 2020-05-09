package com.hhzy.crm.modules.sys.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "sys_log")
@Data
public class SysLog {
    @Id
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户操作
     */
    private String operation;

    /**
     * 操作类型
      */
    private Integer actionType;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 执行时长(毫秒)
     */
    private Long time;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private Integer client;

}