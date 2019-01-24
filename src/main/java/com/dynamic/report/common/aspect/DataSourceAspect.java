package com.dynamic.report.common.aspect;

import com.dynamic.report.entity.DataSourceInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Map;

@Aspect
@Component
public class DataSourceAspect {

    public static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut(value = "execution( * com.dynamic.report.controller..*.*(..))")
    public void execute() {}

    @Around("execute()")
    public void aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        DataSourceInfo dataSourceInfo = new DataSourceInfo();
        Field[] fields = DataSourceInfo.class.getFields();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
        }

        System.out.println("aroundBeforeAdvice ====> ");



        joinPoint.proceed();
        System.out.println("aroundAfterAdvice ====> ");
    }

    private HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

    private Map<String, Field> getField() {

    }

}
