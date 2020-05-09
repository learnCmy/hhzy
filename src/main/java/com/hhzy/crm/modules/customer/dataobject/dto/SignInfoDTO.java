package com.hhzy.crm.modules.customer.dataobject.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: cmy
 * @Date: 2019/11/29 20:50
 * @Description:
 */
@Data
public class SignInfoDTO {

    /**
     * 项目id
     */
    private Long projectId;

    private Long id;

    /**
     * 房屋Id
     */
    private Long houseId;

    /**
     * 签约日期
     */
    private Date signTime;

    /**
     * 签约总价
     */
    private BigDecimal signAmountPrice;

    /**
     * 见面单价
     */
    private BigDecimal signAcreagePrice;

    /**
     * 付款方式
     */
    private String payWay;

    /**
     * 首付金额
     */
    private BigDecimal firstPayPrice;

    /**
     * 贷款金额
     */
    private BigDecimal loansPrice;

    /**
     * 是否交件
     */
    private Boolean isSubmit;


    /**
     * 是否网签
     */
    private Boolean isNetSign;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 备案号
     */
    private String recordNo;

    /**
     * 备注
     */
    private String remark;


    private Long offerBuyId;



    /**
     * 行别
     */
    private  String bankCategory;

    private Date loanTime;

    //是否备案
    private Boolean isRecord;


    //业务宗号
    private String businessNo;

}
