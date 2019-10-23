package com.hhzy.crm.modules.customer.dataobject.dto;

import com.hhzy.crm.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/8/10 17:32
 * @Description:
 */
@Data
@ApiModel
public class CustomerDTO extends BaseEntity {

    private Long  projectId;

    private String keyWord;

    @ApiModelProperty("职业顾问Id")
    private Integer userId;

    /**
     * 来访开始日期
     */
    private Date startComingTime;

    private Date endComingTime;

    @ApiModelProperty("排序字段")
    private String sortClause = "comingTime";

    @ApiModelProperty("排序方式 asc ： 正序， desc ： 倒序")
    private String sort = "desc";




}
