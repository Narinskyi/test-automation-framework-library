package com.onarinskyi.config;

import com.onarinskyi.environment.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:user.properties")
public class UserConfig {

    @Bean
    public User adminUser(@Value("${admin.name}") String adminName,
                          @Value("${admin.password}") String adminPassword,
                          @Value("${admin.email}") String adminEmail) {
        return new User(adminName, adminPassword, adminEmail);
    }

    @Bean
    public User user(@Value("${user.name}") String userName,
                     @Value("${user.password}") String userPassword,
                     @Value("${user.email}") String userEmail) {
        return new User(userName, userPassword, userEmail);
    }
}
