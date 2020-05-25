package com.hhzy.crm.modules.customer.dataobject.dto;

import com.hhzy.crm.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/8/5 08:58
 * @Description:
 */
@Data
public class HouseDTO extends BaseEntity {

    private Long projectId;

    private String keyWord;

    private Integer status;


    private Integer type;

    /**
     * 建筑面积
     */
    private BigDecimal maxAcreage;


    private BigDecimal minAcreage;

    /**
     * 建面单价
     */
    private BigDecimal maxAcreagePrice;

    private BigDecimal minAcreagePrice;

    /**
     * 商铺总价
     */
    private BigDecimal maxAmountPrice;

    private BigDecimal minAmountPrice;


    private Date startTime;

    private Date endTime;

    @ApiModelProperty("排序字段")
    private String sortClause = "createTime";

     @ApiModelProperty("排序方式 asc ： 正序， desc ： 倒序")
    private String sort = "desc";

     private Integer houseTypePermission;

}
