package com.hhzy.crm.common.aspect;

import com.alibaba.fastjson.JSON;
import com.hhzy.crm.common.annotation.DataLog;
import com.hhzy.crm.common.utils.HttpContextUtils;
import com.hhzy.crm.common.utils.IPUtils;
import com.hhzy.crm.modules.sys.entity.SysLog;
import com.hhzy.crm.modules.sys.entity.SysUser;
import com.hhzy.crm.modules.sys.service.SysLogService;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 系统日志，切面处理类
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017年3月8日 上午11:07:35
 */
@Aspect
@Component
public class SysLogAspect {


	@Autowired
	private SysLogService sysLogService;

	@Pointcut("@annotation(com.hhzy.crm.common.annotation.DataLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point,time);


		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		DataLog dataLog = method.getAnnotation(DataLog.class);
		if (dataLog!=null){
			//注解上的描述
			if (dataLog.client().equals("1")){
				sysLog.setOperation("(网页)"+dataLog.value());
			}else if (dataLog.client().equals("2")){
				sysLog.setOperation("(小程序)"+dataLog.value());
			}
			sysLog.setActionType(Integer.valueOf(dataLog.actionType()));
			sysLog.setClient(Integer.valueOf(dataLog.client()));

		}
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		try {
			String s = JSON.toJSONString(args[0]);
			sysLog.setParams(s);
		}catch (Exception e){

		}

		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置Ip
		sysLog.setIp(IPUtils.getIpAddr(request));

		//用户名
		String username = ((SysUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
		sysLog.setUsername(username);

		sysLog.setTime(time);
		sysLog.setCreateTime(new Date());

		sysLogService.save(sysLog);

	}
}
