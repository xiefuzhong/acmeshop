package com.acme.acmemall.acpect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * 所有的API层的拦截，方法的执行时间长度，
 */
@Aspect
@Component
public class ControllerAspect {

    protected Logger logger = Logger.getLogger(getClass());

    @Pointcut("execution(* com.acme.acmemall.controller.*.*(..))")
    private void point() {
        logger.info("-----------------------point----------------------------");
    }

    @Around("point()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();
        long cost = end-begin;
        logger.info("Controller method："+pjp.getSignature().toString()+"，prams："+(pjp.getArgs().length>0? Arrays.toString(pjp.getArgs()):"void")+"，cost time："+cost+" ms");
        return result;
    }

}
