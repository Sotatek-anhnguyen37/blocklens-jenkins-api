package microservices.email;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import web_core.WebApi;

import static constants.Url.GMAIL_ADDRESS;

public class EmailLoginPage extends WebApi {
    @FindBy(css = "h1")
    private WebElement title;
    @FindBy(xpath = "//span[text()='Next']")
    private WebElement nextBtn;
    private static final String INPUT_TEXT_BOX = "//input[@aria-label='%s']";

    @Step("Open Gmail Page")
    public EmailLoginPage openGmailPage() {
        openAnyUrl(GMAIL_ADDRESS);
        waitForPageLoaded();
        return this;
    }

    @Step("Input email address")
    public EmailLoginPage inputDataInTextBox(String emailOrPass, String titleTextBox) {
        sendKeyToElement(INPUT_TEXT_BOX, emailOrPass, titleTextBox);
        return this;
    }

    @Step("Click Next button email signIn")
    public EmailLoginPage clickSignInNextBtn() {
        clickElement(nextBtn);
        return this;
    }

    @Step("Click Next button password sigIn")
    public EmailMainPage clickPassNextBtn() {
        clickElement(nextBtn);
        return new EmailMainPage();
    }

    @Step("Verify Email details Login")
    public EmailLoginPage isOnEmailLoginPage(String nameTextBox) {
        waitForPageLoaded();
        waitForElementVisible(title);
        waitForElementVisible(INPUT_TEXT_BOX, nameTextBox);
        Assert.assertTrue(isControlEnabled(INPUT_TEXT_BOX, nameTextBox));
        Assert.assertTrue(isControlEnabled(nextBtn));
        return this;
    }
}
