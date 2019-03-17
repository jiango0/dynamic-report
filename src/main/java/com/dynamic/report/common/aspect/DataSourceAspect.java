package com.dynamic.report.common.aspect;

import com.dynamic.report.common.datasource.SwitchDataSource;
import com.dynamic.report.entity.DataSourceInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class DataSourceAspect {

    public static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut(value = "execution( * com.dynamic.report.controller..*.*(..))")
    public void execute() {}

    @Around("execute()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            logger.info(" DataSourceAspect begin =>> ");
            HttpServletRequest request = getRequest();
            DataSourceInfo dataSourceInfo = createDataSourceInfo(request);
            SwitchDataSource.setDataSources(dataSourceInfo);
            return joinPoint.proceed();
        } finally {
            SwitchDataSource.clear();
            logger.info(" DataSourceAspect end =>> ");
        }
    }

    private HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

    private DataSourceInfo createDataSourceInfo(HttpServletRequest request) {
        Map<String, String> param = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            param.put(name, value);
        }
        DataSourceInfo dataSourceInfo = new DataSourceInfo();

        Class<? extends DataSourceInfo> clazz = DataSourceInfo.class;
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            String methodStr = method.getName().substring(3).toLowerCase();
            if(param.containsKey(methodStr) && "set".equals(method.getName().substring(0, 3)) ) {
                try {
                    String p = param.get(methodStr);
                    if ("id".equals(methodStr)) {
                        method.invoke(dataSourceInfo, Long.valueOf(param.get(methodStr)));
                    } else {
                        method.invoke(dataSourceInfo, param.get(methodStr));
                    }
                } catch (Exception e) {
                    logger.error("设置参数错误" + "name =" + methodStr + " value = " + param.get(methodStr) , e);
                }
            }
        }

        return dataSourceInfo;
    }

}
