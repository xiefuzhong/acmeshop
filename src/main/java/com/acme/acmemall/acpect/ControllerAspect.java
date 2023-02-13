package com.acme.acmemall.acpect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * 所有的API层的拦截，方法的执行时间长度，
 */
@Aspect
@Component
public class ControllerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(* com.acme.acmemall.controller.*.*(..))")
    private void point() {
        LOGGER.info("-----------------------point----------------------------");
    }

    @Around("pointCutMethodController()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.nanoTime();
        Object result = pjp.proceed();
        long end = System.nanoTime();
        LOGGER.info("Controller method：{}，prams：{}，cost time：{} ns，cost：{} ms",
                pjp.getSignature().toString(), Arrays.toString(pjp.getArgs()), (end - begin), (end - begin) / 1000000);
        return result;
    }

}
