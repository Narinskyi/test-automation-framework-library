package com.onarinskyi.context;

import com.onarinskyi.utils.Report;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class TestNGSuiteListener implements ISuiteListener {
    @Override
    public void onStart(ISuite iSuite) {

    }

    @Override
    public void onFinish(ISuite iSuite) {
        Report.generate();
    }
}