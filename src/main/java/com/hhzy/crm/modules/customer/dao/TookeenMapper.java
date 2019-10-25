package com.hhzy.crm.modules.customer.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.TookeenDTO;
import com.hhzy.crm.modules.customer.dataobject.vo.Top10VO;
import com.hhzy.crm.modules.customer.entity.Tookeen;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TookeenMapper extends MyMapper<Tookeen> {

    List<Tookeen> selectList(TookeenDTO tookeenDTO);

    Integer removeUserId(@Param("ids") List<Long> ids);

    List<Top10VO> selectTop10(@Param("projectId") Long projectId);
}