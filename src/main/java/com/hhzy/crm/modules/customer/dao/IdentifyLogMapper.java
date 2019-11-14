package com.hhzy.crm.modules.customer.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.IdentifyDTO;
import com.hhzy.crm.modules.customer.entity.IdentifyLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IdentifyLogMapper extends MyMapper<IdentifyLog> {

    List<IdentifyLog> selcetByKeyWord(@Param("keyWord") String keyWord,@Param("projectId") Long projectId, @Param("userId") Long userId);

    List<IdentifyLog> selectList(@Param("identifyDTO") IdentifyDTO identifyDTO);

    /**
     * 小程序删除客户 把 置业顾问变为null
     */

    Integer removeUserId(@Param("ids") List<Long> ids);

    @Select("select mobile from identify_log where project_id=#{projectId}")
    List<String> selectMobile(@Param("projectId") Long projectId);


    int countNumberBySellStatus(@Param("projectId") Long projectId, @Param("status") Integer status);
}