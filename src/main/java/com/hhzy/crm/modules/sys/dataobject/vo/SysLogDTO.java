package com.hhzy.crm.modules.sys.dataobject.vo;

import com.hhzy.crm.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/11/23 16:18
 * @Description:
 */
@Data
public class SysLogDTO extends BaseEntity {

    private String keyWord;

    private Integer client;

    private Date startTime;

    private Date endTime;
}
