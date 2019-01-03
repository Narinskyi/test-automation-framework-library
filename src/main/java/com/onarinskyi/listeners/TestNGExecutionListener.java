package com.onarinskyi.listeners;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestNGExecutionListener extends TestListenerAdapter {

    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("  _____                _       ____    _                    _                _ \n" +
                " |_   _|   ___   ___  | |_    / ___|  | |_    __ _   _ __  | |_    ___    __| |\n" +
                "   | |    / _ \\ / __| | __|   \\___ \\  | __|  / _` | | '__| | __|  / _ \\  / _` |\n" +
                "   | |   |  __/ \\__ \\ | |_     ___) | | |_  | (_| | | |    | |_  |  __/ | (_| |\n" +
                "   |_|    \\___| |___/  \\__|   |____/   \\__|  \\__,_| |_|     \\__|  \\___|  \\__,_|\n" +
                "                                                                               ");
        log.info("Starting " + result.getTestClass().getRealClass().getName() + " > " +
                result.getMethod().getMethodName() + " test");
        System.out.println();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println((char) 27 + "[32m" +
                "  _____                _       ____                                   _ \n" +
                " |_   _|   ___   ___  | |_    |  _ \\    __ _   ___   ___    ___    __| |\n" +
                "   | |    / _ \\ / __| | __|   | |_) |  / _` | / __| / __|  / _ \\  / _` |\n" +
                "   | |   |  __/ \\__ \\ | |_    |  __/  | (_| | \\__ \\ \\__ \\ |  __/ | (_| |\n" +
                "   |_|    \\___| |___/  \\__|   |_|      \\__,_| |___/ |___/  \\___|  \\__,_|\n" +
                "                                                                        "
                + (char) 27 + "[0m");
        log.info("Test: " + result.getTestClass().getRealClass().getName() + " > " +
                result.getMethod().getMethodName() + " passed");
        System.out.println();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.err.println("  _____                _       _____           _   _              _ \n" +
                " |_   _|   ___   ___  | |_    |  ___|   __ _  (_) | |   ___    __| |\n" +
                "   | |    / _ \\ / __| | __|   | |_     / _` | | | | |  / _ \\  / _` |\n" +
                "   | |   |  __/ \\__ \\ | |_    |  _|   | (_| | | | | | |  __/ | (_| |\n" +
                "   |_|    \\___| |___/  \\__|   |_|      \\__,_| |_| |_|  \\___|  \\__,_|\n" +
                "                                                                    ");
        log.info("Test: " + result.getTestClass().getRealClass().getName() + " > " +
                result.getMethod().getMethodName() + " failed");
        System.out.println();
    }
}