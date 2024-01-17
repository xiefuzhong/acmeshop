package com.acme.acmemall.acpect;

import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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
        long begin = System.nanoTime();
        Object returnValue = null;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 请求的方法名
        String methodName = signature.getName();
        try {
            returnValue = pjp.proceed();
        } catch (Throwable throwable) {
            String errorMessage = String.format("请求异常：请求方法：{s%}, 异常信息：{s%}", methodName, Throwables.getStackTraceAsString(throwable));
            logger.error(errorMessage);
            throw throwable;
        }
        long end = System.nanoTime();
        long cost = end - begin;
        logger.info("Controller method：" + pjp.getSignature().getName() + "，prams：" + (pjp.getArgs().length > 0 ? Arrays.toString(pjp.getArgs()) : "void") + "，cost time：" + cost + " ns");
        return returnValue;
    }

}
