package com.onarinskyi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.onarinskyi")
@EnableTransactionManagement
public class AppConfig {
    public static void main(String[] args) {

    }
}