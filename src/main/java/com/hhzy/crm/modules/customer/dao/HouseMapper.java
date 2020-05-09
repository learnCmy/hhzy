package com.hhzy.crm.modules.customer.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.HouseDTO;
import com.hhzy.crm.modules.customer.entity.House;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper extends MyMapper<House> {

    List<House> selectList(@Param("houseDTO") HouseDTO houseDTO);

    void updateStatus(@Param("ids") List<Long> ids,@Param("status") Integer status);
}