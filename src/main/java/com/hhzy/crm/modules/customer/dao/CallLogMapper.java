package com.hhzy.crm.modules.customer.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.CallLogDTO;
import com.hhzy.crm.modules.customer.entity.CallLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CallLogMapper extends MyMapper<CallLog> {

    List<CallLog> list(@Param("callLogDTO") CallLogDTO callLogDTO);

    Integer removeUserId(@Param("ids") List<Long> Ids);
}