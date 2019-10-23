package com.hhzy.crm.modules.sys.service;

import com.hhzy.crm.common.base.BaseService;
import com.hhzy.crm.common.response.CommonResult;
import com.hhzy.crm.modules.sys.entity.SysUserToken;

/**
 * @Auther: cmy
 * @Date: 2019/7/31 17:37
 * @Description:
 */
public interface SysUserTokenService  extends BaseService<SysUserToken> {

    /**
     * 生成token
     * @param userId  用户ID
     */
    CommonResult createToken(long userId);

    /**
     * 退出，修改token值
     * @param userId  用户ID
     */
    void logout(long userId);
}
