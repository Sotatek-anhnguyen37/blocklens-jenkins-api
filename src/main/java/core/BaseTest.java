package core;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import util.TestNgCustomListener;

import static constants.Url.BASE_URL;

@Listeners({TestNgCustomListener.class})
public class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void initSuite(){
        BaseApi.initReqSpec();
        BaseApi.setBaseUrl(BASE_URL);
    }
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        BaseApi.initReqSpec();
        BaseApi.setBaseUrl(BASE_URL);
    }
    @BeforeClass(alwaysRun = true)
    public void beforeClass(){
        BaseApi.initReqSpec();
        BaseApi.setBaseUrl(BASE_URL);
    }
}
