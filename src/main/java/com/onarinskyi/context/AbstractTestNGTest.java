package com.onarinskyi.context;

import com.onarinskyi.config.AppConfig;
import com.onarinskyi.listeners.TestNGExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import javax.annotation.PostConstruct;

@Listeners(TestNGExecutionListener.class)
@ContextConfiguration(classes = AppConfig.class)
@TestExecutionListeners(inheritListeners = false,
        listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public abstract class AbstractTestNGTest extends AbstractTestNGSpringContextTests {

    protected SoftAssert softly = new SoftAssert();

    @PostConstruct
    public void initAppContext() {
        ApplicationContextManager.initWith(applicationContext);
    }
}

