package com.onarinskyi.utils;


import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.PropertyResourceBundle;

public class Report {

    private static final Logger logger = Logger.getLogger(Report.class);
    private static final String ENVIRONMENT_FILE = "target/allure-results/environment.properties";
    private static final String BASE_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator;

    public static void generate() {
        logger.info("Generating environment attachment to Allure report");

        try {
            PropertyResourceBundle driverProperties = new PropertyResourceBundle(new FileInputStream(BASE_PATH + "driver.properties"));

            String env = System.getProperty("env");

            String baseUrl = env == null ? driverProperties.getString("default.base.url") :
                    driverProperties.getString(env.concat(".base.url"));
            String browser = driverProperties.getString("browser.type");

            FileUtils.touch(new File(ENVIRONMENT_FILE));
            FileOutputStream out = new FileOutputStream(ENVIRONMENT_FILE);

            Properties environmentProperties = new Properties();
            environmentProperties.put("base.url", baseUrl);
            environmentProperties.put("browser", browser);

            environmentProperties.store(out, "Environment Properties");
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.warn("Generating environment attachment to Allure report failed");
            e.printStackTrace();
        }
    }
}
