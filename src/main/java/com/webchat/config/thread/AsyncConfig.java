package com.webchat.config.thread;

import com.webchat.common.exception.AsyncException;
import com.webchat.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Autowired
    @Qualifier(value = "bizExecutor")
    private Executor executor;

    @Override
    public Executor getAsyncExecutor() {
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            log.info("Async method: {} has uncaught exception,params:{}",
                    method.getName(), JsonUtil.toJsonString(params));
            if (ex instanceof AsyncException) {
                AsyncException asyncException = (AsyncException) ex;
                log.info("asyncException:{}", asyncException.getMessage());
            } else {
                log.info("Exception :");
                ex.printStackTrace();
            }
        }
    }
}
