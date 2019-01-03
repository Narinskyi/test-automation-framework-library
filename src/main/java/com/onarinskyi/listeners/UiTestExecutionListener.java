package com.onarinskyi.listeners;

import com.onarinskyi.context.ApplicationContextManager;
import com.onarinskyi.driver.WebDriverDecorator;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class UiTestExecutionListener extends TestListenerAdapter {

    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onTestFailure(ITestResult result) {
        ApplicationContextManager.getApplicationContext()
                .getBean(WebDriverDecorator.class).takeScreenshot();
    }
}