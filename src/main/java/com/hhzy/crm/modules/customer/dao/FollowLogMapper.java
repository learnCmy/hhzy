package com.hhzy.crm.modules.customer.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.customer.dataobject.dto.FollowLogDTO;
import com.hhzy.crm.modules.customer.entity.FollowLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowLogMapper extends MyMapper<FollowLog> {

    List<FollowLog> selcetByKeyWord(@Param("keyWord") String keyWord,@Param("projectId") Long projectId,@Param("userId") Long userId);


    List<FollowLog> selectFollowLog(@Param("followLogDTO") FollowLogDTO followLogDTO);


    /**
     * 小程序删除客户 把 置业顾问变为null
     */

    Integer removeUserId(@Param("ids") List<Long> ids);
}