package com.hhzy.crm.modules.customer.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.CustomerDTO;
import com.hhzy.crm.modules.customer.dataobject.vo.Top10VO;
import com.hhzy.crm.modules.customer.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper extends MyMapper<Customer> {


    List<Customer> selctList(@Param("customerDTO") CustomerDTO customerDTO);

    List<Top10VO> selectTop10Cusomter(@Param("projectId") Long projectId);

    @Select("select mobile from customer where project_id=#{projectId}")
    List<String> selectMobileByProject(@Param("projectId") Long projectId);


    Integer removeUserId(@Param("customerIds") List<Long> customerIds);
}