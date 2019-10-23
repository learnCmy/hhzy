package com.hhzy.crm.modules.customer.service.impl;

import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.modules.customer.dao.TookeenMapper;
import com.hhzy.crm.modules.customer.entity.Tookeen;
import com.hhzy.crm.modules.customer.service.CallLogService;
import com.hhzy.crm.modules.customer.service.CustomerService;
import com.hhzy.crm.modules.customer.service.TookeenService;
import com.hhzy.crm.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: cmy
 * @Date: 2019/10/21 17:43
 * @Description:
 */
@Service
public class TookeenServiceImpl extends BaseServiceImpl<Tookeen> implements TookeenService {

    @Autowired
    private TookeenMapper tookeenMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CallLogService callLogService;

    @Override
    public void saveBasicTookeen(Tookeen tookeen) {
        String mobile = tookeen.getMobile();
        Long projectId = tookeen.getProjectId();



    }

    @Override
    public void updateBasicTookeen(Tookeen tookeen) {

    }
}
