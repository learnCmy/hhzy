package com.hhzy.crm.modules.sys.service.impl;

import com.google.common.collect.Maps;
import com.hhzy.crm.common.base.BaseServiceImpl;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.sys.entity.SysUserToken;
import com.hhzy.crm.modules.sys.oauth2.TokenGenerator;
import com.hhzy.crm.modules.sys.service.SysUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 17:38
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class SysUserTokenServiceImpl extends BaseServiceImpl<SysUserToken> implements SysUserTokenService {
    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Override
    public CommonResult createToken(long userId) {

        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        SysUserToken tokenEntity = this.queryById(userId);
        if(tokenEntity == null){
            tokenEntity = new SysUserToken();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //保存token
            this.save(tokenEntity);
        }else{
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //更新token
            this.update(tokenEntity);
        }

        HashMap<Object, Object> map = Maps.newHashMap();
        map.put("token",token);
        map.put("expire",EXPIRE);
        return CommonResult.success(map);
    }

    @Override
    public void logout(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //修改token
        SysUserToken tokenEntity = new SysUserToken();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        this.update(tokenEntity);
    }
}
