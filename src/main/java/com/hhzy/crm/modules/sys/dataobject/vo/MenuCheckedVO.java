package com.hhzy.crm.modules.sys.dataobject.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/8/14 21:35
 * @Description:
 */
@Data
public class MenuCheckedVO {


    private List<Long> checked;

    private List<Long> halfCheckedKeys;
}
