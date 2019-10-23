package com.hhzy.crm.common.exception;

import com.hhzy.crm.common.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @Auther: cmy
 * @Date: 2019/7/29 11:45
 * @Description:
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {


    @ExceptionHandler(BusinessException.class)
    public CommonResult handleBusinessException(BusinessException e){
        log.error(e.getMessage(), e);
        CommonResult<Object> result = new CommonResult<>();
        result.setMsg(e.getMsg());
        result.setStatus(e.getCode());
        return result;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResult handlerNoFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return CommonResult.build(404,"路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public CommonResult handleDuplicateKeyException(DuplicateKeyException e){
        log.error(e.getMessage(), e);
        return CommonResult.build(500,"数据库中已存在该记录");
    }

    @ExceptionHandler(AuthorizationException.class)
    public CommonResult handleAuthorizationException(AuthorizationException e){
        log.error(e.getMessage(), e);
        return CommonResult.build(402,"没有权限，请联系管理员授权");
    }

    @ExceptionHandler(Exception.class)
    public CommonResult handleException(Exception e){
        log.error(e.getMessage(), e);
        return CommonResult.build(500,"服务异常请联系管理员");
    }
}
