package com.hhzy.crm.modules.customer.dataobject.dto;

import com.hhzy.crm.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2020/5/22 11:10
 * @Description:
 */
@Data
public class ReportDTO extends BaseEntity {

    private Long userId;

    private String keyWord;

    private Long projectId;

    private Date startTime;

    private Integer productType;

    private String sourceWay;

    private Date endTime;

    @ApiModelProperty("排序字段")
    private String sortClause = "createTime";

    @ApiModelProperty("排序方式 asc ： 正序， desc ： 倒序")
    private String sort = "desc";
}
