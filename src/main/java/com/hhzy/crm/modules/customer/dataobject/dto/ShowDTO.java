package com.hhzy.crm.modules.customer.dataobject.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/9/24 22:17
 * @Description:
 */
@Data
public class ShowDTO {

    private Long projectId;

    private String dateType;//"date" "week" "month" //按天 按月 按年

    private Date startTime;

    private Date endTime;
}
