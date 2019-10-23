package com.hhzy.crm.modules.customer.dataobject.dto;

import com.hhzy.crm.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/8/31 21:53
 * @Description:
 */
@Data
public class IdentifyDTO extends BaseEntity {

    private Long projectId;

    private Long userId;

    private String keyWord;

    private Date startTime;

    private Date endTime;

    //vip卡号
    private String vipCard;

    @ApiModelProperty("排序字段")
    private String sortClause="createTime";

    @ApiModelProperty("排序方式 asc ： 正序， desc ： 倒序")
    private String sort="desc";

}
