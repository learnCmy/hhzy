package com.hhzy.crm.modules.customer.dataobject.dto;

import com.hhzy.crm.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/9/2 11:31
 * @Description:
 */
@Data
public class SignDTO  extends BaseEntity {


    private Long projectId;


    private Date signStartTime;

    private Date  signEndTime;

    private String dateType; //'sign' 'offer' 'loan'

    /**
     * 合同编号
     */
    private String contractNo;

    private String mobile;

    private String name;

    private Long userId;

    private Long houseId;

    private String keyWord;


    /**
     * 房源类别 1- 住宅 2-商铺
     */
    private Integer houseType;

    private String bankCategory;

    private Boolean confirmInfo;


    @ApiModelProperty("排序字段")
    private String sortClause="signTime";

    @ApiModelProperty("排序方式 asc ： 正序， desc ： 倒序")
    private String sort="desc";



}
