package com.hhzy.crm.modules.customer.dataobject.dto;

import com.hhzy.crm.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/8/10 21:56
 * @Description:
 */
@Data
@ApiModel
public class CallLogDTO  extends BaseEntity {

    /**
     * 项目Id
     */
    private Long projectId;
    //置业顾问id
    private Long userId;
    //关键字 手机号 姓名
    private String keyWord;
    //来电日期
    private Date startCallTime;

    private Date endCallTime;

    @ApiModelProperty("获取途径")
    private String acquiringWay;

    @ApiModelProperty("询问内容")
    private String askContent;

    @ApiModelProperty("排序字段")
    private String sortClause="callTime";

    @ApiModelProperty("排序方式 asc ： 正序， desc ： 倒序")
    private String sort="desc";

}
