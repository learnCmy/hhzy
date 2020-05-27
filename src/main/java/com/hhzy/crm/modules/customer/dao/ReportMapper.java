package com.hhzy.crm.modules.customer.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.ReportDTO;
import com.hhzy.crm.modules.customer.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper extends MyMapper<Report> {

    List<Report> selectList(ReportDTO reportDTO);

    Integer removeUserId(@Param("ids") List<Long> ids);

}