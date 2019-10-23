package com.hhzy.crm.modules.customer.dataobject.importPOI;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: cmy
 * @Date: 2019/10/4 20:00
 * @Description:
 */
@Data
public class HouseImport {

    /**
     * 商铺信息
     */
    @Excel(name ="商铺信息" )
    private String houseInfo;

    /**
     * 建筑面积
     */
    @Excel(name = "建筑面积（㎡）")
    private BigDecimal acreage;

    /**
     * 建面单价
     */
    @Excel(name = "建面单价（元）")
    private BigDecimal acreagePrice;

    /**
     * 商铺总价
     */
    @Excel(name = "商铺总价（元）")
    private BigDecimal amountPrice;


    /**
     * 房源类别 1- 住宅 2-商铺
     */
    private Integer type;

    /**
     * 楼层
     */
    @Excel(name = "楼层")
    private String floorLevel;

    /**
     * 几幢
     */
    @Excel(name = "楼栋")
    private String buildNo;

    /**
     * 房号
     */
    @Excel(name = "房号")
    private String roomNo;



}
