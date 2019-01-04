package com.onarinskyi.context;

import com.onarinskyi.driver.WebDriverDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;

@Listeners({UiTestExecutionListener.class, TestNGSuiteListener.class})
public abstract class AbstractUITest extends AbstractTest {

    @Autowired
    protected WebDriverDecorator driver;

    @Autowired
    private ThreadLocal<WebDriverDecorator> threadLocal;

    @AfterClass(alwaysRun = true)
    public void quitDriver() {
        driver.quit();
        threadLocal.remove();
    }

    WebDriverDecorator getDriver() {
        return driver;
    }
}

