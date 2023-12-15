package microservices.email;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import web_core.WebApi;

public class EmailMainPage extends WebApi {
    @FindBy(xpath = "//a[contains(text(),'Inbox')]")
    private WebElement InboxOption;
    @FindBy(xpath = "//div[text()='Delete']")
    private WebElement deleteButtonIcon;
    private String title = "(//span[text()='%s'])[2]";

    private String unreadEmailByTitle = "//tr[contains(.,'unread')]//div[@role='link']//span[contains(text(),'%s')]";
    private String emailByTitle = "//div[@role='link']//span[contains(text(),'%s')]";

    @Step("Verify title email")
    public EmailMainPage isOnEmailMainPage(String titleDetailEmail) {
        waitForPageLoaded();
        waitForElementVisible(unreadEmailByTitle, titleDetailEmail);
        Assert.assertTrue(isControlDisplayed(unreadEmailByTitle, titleDetailEmail));
        return this;
    }

    @Step("Open unread email")
    public EmailDetailPage openLatestUnreadEmail(String titleEmail){
        clickElement(unreadEmailByTitle, titleEmail);
        return new EmailDetailPage();
    }

    @Step("Right click")
    public EmailMainPage rightClickOnTheTitleEmail(String titleEmail){
        waitForElementVisible(emailByTitle, titleEmail);
        rightClickElement(emailByTitle, titleEmail);
        return this;
    }

    @Step("Click Delete option")
    public EmailMainPage clickDeleteOption(String titleEmail){
        clickElement(deleteButtonIcon);
        Assert.assertTrue(isControlUnDisplayed(emailByTitle, titleEmail));
        return this;
    }
}
