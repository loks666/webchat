package com.webchat.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringContextUtil implements ApplicationContextAware, InitializingBean {

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    public void afterPropertiesSet() throws Exception {
        log.debug("after Properties Set, applicationContext:{},currentThread:{}",
                applicationContext, Thread.currentThread());

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
        log.debug("set Application Context, applicationContext:{},currentThread:{}",
                applicationContext, Thread.currentThread());

    }

    /**
     * 获取对象 这里重写了bean方法，起主要作用
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public static Object getBeanName(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBeanType(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    public static Resource getResource(String location) {
        return applicationContext.getResource(location);
    }

}
