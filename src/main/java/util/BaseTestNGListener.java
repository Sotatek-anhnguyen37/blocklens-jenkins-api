package util;

import io.qameta.allure.listener.TestLifecycleListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Map;

public class BaseTestNGListener implements ITestListener, TestLifecycleListener {


    public BaseTestNGListener() {

    }

    @Override
    public void onTestStart(ITestResult result) {
        LogHelper.getInstance().info("Test started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogHelper.getInstance().info("Test stop with SUCCESS: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogHelper.getInstance().info("Test stop with FAILED: " + result.getName());
        if (result.getThrowable() != null) {
            result.getThrowable().printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogHelper.getInstance().info("Test stop with SKIPPED: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext testContext) {
        LogHelper.getInstance().info("----------------Test Finished----------------");
    }

}
