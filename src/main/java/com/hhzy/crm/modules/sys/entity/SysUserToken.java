package com.hhzy.crm.modules.sys.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 系统用户Token
 */
@Table(name = "sys_user_token")
@Data
public class SysUserToken implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	//token
	private String token;
	//过期时间
	private Date expireTime;
	//更新时间
	private Date updateTime;

}
