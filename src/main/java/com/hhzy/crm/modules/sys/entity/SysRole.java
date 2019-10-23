package com.hhzy.crm.modules.sys.entity;


import com.hhzy.crm.modules.sys.dataobject.vo.MenuCheckedVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 * 
 */
@Data
@Table(name = "sys_role")
@ApiModel
public class SysRole implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色ID
	 */
	@ApiModelProperty("角色Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long roleId;

	/**
	 * 角色名称
	 */
	@NotBlank(message="角色名称不能为空")
	private String roleName;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
	
	/**
	 * 创建者ID
	 */
	@ApiModelProperty("创建者Id 好像暂时没用")
	private Long createUserId;

	@Transient
	private List<Long> menuIdList;

	@Transient
	private MenuCheckedVO menuCheckedVO;

	/**
	 * 创建时间
	 */
	private Date createTime;


	
}
