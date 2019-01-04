package com.onarinskyi.context;

import com.onarinskyi.config.AppConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

@Listeners(TestNGExecutionListener.class)
@ContextConfiguration(classes = AppConfig.class)
@TestExecutionListeners(inheritListeners = false,
        listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public abstract class AbstractTest extends AbstractTestNGSpringContextTests {

    protected SoftAssert softly = new SoftAssert();
}