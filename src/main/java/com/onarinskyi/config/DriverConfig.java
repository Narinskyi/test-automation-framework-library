package com.onarinskyi.config;

import com.onarinskyi.driver.WebDriverDecorator;
import com.onarinskyi.driver.WebDriverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DriverConfig {

    @Autowired
    private WebDriverFactory driverFactory;

    @Bean
    public ThreadLocal<WebDriverDecorator> threadLocal() {
        return ThreadLocal.withInitial(driverFactory::getInitialDriver);
    }

    @Bean
    @Scope("prototype")
    public WebDriverDecorator driver(ThreadLocal<WebDriverDecorator> threadLocal) {
        return threadLocal.get();
    }
}