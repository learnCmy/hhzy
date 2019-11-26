package com.hhzy.crm.modules.customer.dataobject.dto;

import com.hhzy.crm.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/9/1 14:24
 * @Description:
 */
@Data
public class OfferBuyDTO extends BaseEntity {

    private Long projectId;

    private Long userId;

    private String keyWord;

    //认购日期
    private Date offerStartTime;

    private Date offerEndTime;

    private Integer status;

    private Integer confirmInfo ; //1 是 未签约 2是未确认 3 是已确认

    @ApiModelProperty("排序字段")
    private String sortClause="offerBuyTime";

    @ApiModelProperty("排序方式 asc ： 正序， desc ： 倒序")
    private String sort="desc";
}
