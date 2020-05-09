package com.hhzy.crm.modules.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.modules.customer.entity.Customer;
import com.hhzy.crm.modules.sys.dao.SysLogMapper;
import com.hhzy.crm.modules.sys.dataobject.vo.SysLogDTO;
import com.hhzy.crm.modules.sys.entity.SysLog;
import com.hhzy.crm.modules.sys.service.SysLogService;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: cmy
 * @Date: 2019/11/22 09:38
 * @Description:
 */
@Service
@Slf4j
@Transactional
public class SysLogServiceImpl  extends BaseServiceImpl<SysLog> implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public PageInfo<SysLog> selectSysLog(SysLogDTO sysLogDTO) {
        if (sysLogDTO.getPage()!=null&&sysLogDTO.getPageSize()!=null){
            PageHelper.startPage(sysLogDTO.getPage(),sysLogDTO.getPageSize());
        }
        List<SysLog> sysLogs = sysLogMapper.listSysLog(sysLogDTO);
        return new PageInfo<>(sysLogs);
    }
}
