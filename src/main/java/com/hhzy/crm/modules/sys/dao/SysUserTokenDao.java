package com.hhzy.crm.modules.sys.dao;

import com.hhzy.crm.common.base.MyMapper;
import com.hhzy.crm.modules.sys.entity.SysUserToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 17:31
 * @Description:
 */
@Mapper
public interface SysUserTokenDao extends MyMapper<SysUserToken> {


    @Select("select * from sys_user_token where token = #{value}")
    SysUserToken queryByToken(String token);


}
