package com.onarinskyi.context;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class UiTestExecutionListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult result) {
        Object test = result.getInstance();
        if (test instanceof AbstractUITest) {
            ((AbstractUITest) test).getDriver().takeScreenshot();
        }
    }
}