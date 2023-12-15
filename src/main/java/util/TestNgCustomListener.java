package util;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import web_core.PageFactoryManager;
import web_core.WebApi;
import web_core.WebAppDriverManager;

import java.io.File;
import java.io.FileInputStream;

public class TestNgCustomListener extends BaseTestNGListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            PageFactoryManager.get(WebApi.class).switchToLastTab();
            File sc = ((TakesScreenshot) WebAppDriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            Allure.addAttachment("Page source", WebAppDriverManager.getDriver().getPageSource());
            Allure.addAttachment("Failed test image", new FileInputStream(sc));
        }
        catch (NullPointerException nullPointerException){

        }
        catch (Exception e) {
            LogHelper.getInstance().info("Cannot get screenshot");
            LogHelper.getInstance().info(e.getMessage());
        }
        super.onTestFailure(result);
    }
}
