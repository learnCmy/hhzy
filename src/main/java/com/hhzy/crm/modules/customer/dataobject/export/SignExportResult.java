package com.hhzy.crm.modules.customer.dataobject.export;

import com.hhzy.crm.modules.customer.entity.House;
import com.hhzy.crm.modules.customer.entity.OfferBuy;
import com.hhzy.crm.modules.customer.entity.SignInfo;
import lombok.Data;

/**
 * @Auther: cmy
 * @Date: 2019/9/13 21:26
 * @Description:
 */
@Data
public class SignExportResult extends SignInfo {


    private House house;

    private OfferBuy offerBuy;


}
