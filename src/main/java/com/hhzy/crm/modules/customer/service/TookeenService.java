package com.hhzy.crm.modules.customer.service;

import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.customer.entity.Tookeen;

/**
 * @Auther: cmy
 * @Date: 2019/10/21 17:42
 * @Description:
 */
public interface TookeenService extends BaseService<Tookeen> {


    void  saveBasicTookeen(Tookeen tookeen);

    void updateBasicTookeen(Tookeen tookeen);


}
