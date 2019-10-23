package com.hhzy.crm.modules.customer.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "sign_info")
@Data
public class SignInfo {

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Long projectId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 房屋Id
     */
    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "user_id")
    private Long userId;


    /**
     * 签约日期
     */
    @Column(name = "sign_time")
    private Date signTime;

    /**
     * 签约总价
     */
    @Column(name = "sign_amount_price")
    private BigDecimal signAmountPrice;

    /**
     * 见面单价
     */
    @Column(name = "sign_acreage_price")
    private BigDecimal signAcreagePrice;

    /**
     * 付款方式
     */
    @Column(name = "pay_way")
    private String payWay;

    /**
     * 首付金额
     */
    @Column(name = "first_pay_price")
    private BigDecimal firstPayPrice;

    /**
     * 贷款金额
     */
    @Column(name = "loans_price")
    private BigDecimal loansPrice;

    /**
     * 是否交件
     */
    @Column(name = "is_submit")
    private Boolean isSubmit;

    /**
     * 是否网签
     */
    @Column(name = "is_net_sign")
    private Boolean isNetSign;

    /**
     * 合同编号
     */
    @Column(name = "contract_no")
    private String contractNo;

    /**
     * 备案号
     */
    @Column(name = "record_no")
    private String recordNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "offer_buy_id")
    private Long offerBuyId;

    /**
     * 交房日期
     */
    @Column(name = "delivery_time")
    private  Date deliveryTime;

    /**
     * 是否结佣
     */
    @Column(name ="commission" )
    private  Boolean commission;

    /**
     * 结算情况
     */
    @Column(name="clear_info")
    private  String clearInfo;


    /**
     * 行别
     */
    @Column(name = "bank_category")
    private  String bankCategory;

    @Column(name = "loan_time")
    private Date loanTime;

    //是否备案
    @Column(name = "is_record")
    private Boolean isRecord;


    //业务宗号
    @Column(name = "business_no")
    private String businessNo;
}