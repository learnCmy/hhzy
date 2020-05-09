package com.hhzy.crm.modules.sys.service;

import com.github.pagehelper.PageInfo;
import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.modules.sys.dataobject.vo.SysLogDTO;
import com.hhzy.crm.modules.sys.entity.SysLog;

/**
 * @Auther: cmy
 * @Date: 2019/11/22 09:30
 * @Description:
 */
public  interface SysLogService  extends BaseService<SysLog> {

    PageInfo<SysLog> selectSysLog(SysLogDTO sysLogDTO);

}
