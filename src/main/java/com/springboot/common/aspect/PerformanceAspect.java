package com.springboot.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect接口性能监控
 */
@Component
@Aspect
@SuppressWarnings("all")
public class PerformanceAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceAspect.class);

    @Before("execution(* com.springboot.controller.*.*(..)) && @annotation(Performance)")
    public void before(){
//		System.out.println("方法执行前");
    }

    @After("execution(* com.springboot.controller.*.*(..)) && @annotation(Performance)")
    public void after(){
//		System.out.println("方法执行后");
    }

    @Around("execution(* com.springboot.controller.*.*(..)) && @annotation(Performance)")
    public Object around(ProceedingJoinPoint jp) throws Throwable{
        Long start = System.currentTimeMillis();
        Object result = jp.proceed();
        Long end = System.currentTimeMillis();
        LOGGER.info(jp.getSignature() +" consumption time : "+ (end-start) + "ms");
        return result;
    }

}
