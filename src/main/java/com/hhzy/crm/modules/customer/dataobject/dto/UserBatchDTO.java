package com.hhzy.crm.modules.customer.dataobject.dto;

import lombok.Data;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/10/14 17:43
 * @Description:
 */
@Data
public class UserBatchDTO {

    /**
     * 数据主键
     */
    private List<Long> ids;

    private Long userId;

}
