package com.hhzy.crm.modules.sys.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.sys.dataobject.vo.SysLogDTO;
import com.hhzy.crm.modules.sys.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysLogMapper extends MyMapper<SysLog> {


    List<SysLog> listSysLog(SysLogDTO sysLogDTO);
}