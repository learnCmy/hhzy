package com.hhzy.crm.modules.sys.dataobject.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/1 11:31
 * @Description:
 */
@Data
public class SysUserInfo {

    private String name;

    private String mobile;

    private List<String> roleNames;

    private List<String> roleIds;

}
