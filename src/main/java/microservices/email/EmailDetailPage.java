package microservices.email;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import web_core.WebApi;

public class EmailDetailPage extends WebApi {
    @FindBy(xpath = "//*[contains(@href,'dev-console.blocklens.io/')]")
    private WebElement linkVerifyEmail;
    @FindBy(xpath = "//a[contains(text(),'Inbox')]")
    private WebElement InboxOption;
    @FindBy(xpath = "(//a[contains(@href,'dev-console.blocklens.io/')])[1]")
    private WebElement verifyEmailOldLink;
    private String titleEmailDetail = "//h2[text()='%s']";

    @Step("Verify title email")
    public EmailDetailPage isOnEmailMainPage(String title) {
        waitForPageLoaded();
        waitForElementVisible(titleEmailDetail, title);
        waitForElementVisible(linkVerifyEmail);
        Assert.assertTrue(isControlDisplayed(titleEmailDetail, title));
        Assert.assertTrue(isControlEnabled(linkVerifyEmail));
        return this;
    }

    @Step("Click Inbox button")
    public EmailMainPage clickInboxButton(){
        clickElement(InboxOption);
        return new EmailMainPage();
    }

    @Step("get token verify email")
    public String getToken(int index){
        return getTextElement(linkVerifyEmail).substring(index);
    }

    @Step("get old token verify email")
    public String getTokenOld(int index){
        return getTextElement(verifyEmailOldLink).substring(index);
    }
}
