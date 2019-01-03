package com.onarinskyi.config;

import com.onarinskyi.environment.Browser;
import com.onarinskyi.environment.OperatingSystem;
import com.onarinskyi.environment.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:driver.properties")
public class EnvironmentConfig {

    @Bean
    public OperatingSystem operatingSystem() {
        return OperatingSystem.current();
    }

    @Bean
    public Browser browser(@Value("${browser.type}") String type) {
        return Browser.of(type);
    }

    @Bean
    public Timeout timeout(@Value("${implicit.wait}") String implicitWait, @Value("${explicit.wait}") String explicitWait) {
        return new Timeout(implicitWait, explicitWait);
    }
}
