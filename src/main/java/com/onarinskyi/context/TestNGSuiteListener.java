package com.onarinskyi.context;

import com.onarinskyi.utils.Report;
import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class TestNGSuiteListener implements ISuiteListener {
    @Override
    public void onFinish(ISuite iSuite) {
        Report.generate();
    }
}