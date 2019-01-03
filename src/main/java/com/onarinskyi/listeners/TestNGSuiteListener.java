package com.onarinskyi.listeners;

import com.onarinskyi.utils.Report;
import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class TestNGSuiteListener implements ISuiteListener {

    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onStart(ISuite iSuite) {

    }

    @Override
    public void onFinish(ISuite iSuite) {
        Report.generate();
    }
}