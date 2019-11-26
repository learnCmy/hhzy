package com.hhzy.crm.modules.customer.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.SignDTO;
import com.hhzy.crm.modules.customer.dataobject.vo.SignVo;
import com.hhzy.crm.modules.customer.entity.SignInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface SignInfoMapper extends MyMapper<SignInfo> {

    List<SignInfo> selectList(@Param("signDTO") SignDTO signDTO);

    List<SignVo> selectSignVoExport(SignDTO signDTO);

    List<SignVo> selectSignVo(SignDTO signDTO);

    BigDecimal sellAmountCount(@Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("projectId")Long projectId
            ,@Param("houseType")Integer houseType);

}