package com.onarinskyi.context;

import org.springframework.context.ApplicationContext;

public class ApplicationContextManager {
    private static ApplicationContext applicationContext;

    static void initWith(ApplicationContext appContext) {
        applicationContext = appContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
