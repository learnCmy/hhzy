package com.hhzy.crm.modules.customer.dataobject.dto;

import com.hhzy.crm.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/8/31 17:27
 * @Description:
 */
@Data
public class FollowLogDTO  extends BaseEntity {


    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 置业顾问ID
     */
    private Long userId;


    private  String keyWord;

    /**
     * 创建时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 下次拜访开始时间
     */
    private Date nextStartTime;


    /**
     * 下次拜访结束时间
     */
    private Date nextEndTime;

    @ApiModelProperty("排序字段")
    private String sortClause = "nextVisitTime";

    @ApiModelProperty("排序方式 asc ： 正序， desc ： 倒序")
    private String sort = "desc";

}
