package my.test.resetPassword;

import constants.APIError;
import constants.APIErrorMessage;
import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.email.EmailDetailPage;
import microservices.email.EmailLoginPage;
import microservices.resetpassword.model.ForgotPasswordInput;
import microservices.resetpassword.model.ResetPasswordInput;
import microservices.resetpassword.step.ResetPasswordSteps;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import web_core.PageFactoryManager;
import web_core.WebAppDriverManager;

import static constants.APIErrorMessage.*;
import static constants.EventsUfpConstants.*;
import static util.CommonUtil.getRandomString;

@Feature("Reset password")
public class ResetPassword extends BaseTest {
    ResetPasswordSteps resetPasswordSteps = new ResetPasswordSteps();
    String email = "testbinh25@gmail.com";
    String newPassWork = "Ngoc@114";
    @BeforeMethod(alwaysRun = true, onlyForGroups = {"browser"})
    public void beforeMethod() {
        WebAppDriverManager.openMultiBrowser("chrome");
    }

    @AfterMethod(alwaysRun = true, onlyForGroups = {"browser"})
    public void closeBrowser() {
        WebAppDriverManager.closeBrowserAndDriver(WebAppDriverManager.getDriver());
    }
    @Test(description = "Forgot and Reset Password successfully", groups = {"regression", "resetPassword", "api-blocklens-users", "browser"})
    public void resetPasswordAndChooseNewPasswordSuccessfully() throws Exception {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(email));
        resetPasswordSteps.then_verifyForgotPassWordSuccessful(MESSAGE_OK);
        EmailLoginPage emailLoginPage = PageFactoryManager.get(EmailLoginPage.class);
        emailLoginPage
                .openGmailPage()
                .isOnEmailLoginPage(NAME_TEXT_BOX_EMAIL_ADDRESS)
                .inputDataInTextBox("testbinh25@gmail.com", NAME_TEXT_BOX_EMAIL_ADDRESS)
                .clickSignInNextBtn()
                .isOnEmailLoginPage(NAME_TEXT_BOX_PASS)
                .inputDataInTextBox("Ngoc@1997", NAME_TEXT_BOX_PASS)
                .clickPassNextBtn()
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .openLatestUnreadEmail(TITLE_EMAIL)
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
        EmailDetailPage emailDetailPage = new EmailDetailPage();
        String token = emailDetailPage.getToken(46);
        //Reset Password
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput(newPassWork, token));
        resetPasswordSteps.then_verifyResetPassWordSuccessful(MESSAGE_OK);
        //Delete email
        emailDetailPage
                .clickInboxButton()
                .rightClickOnTheTitleEmail(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .clickDeleteOption(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
    }
    @Test(description = "Forgot password Unsuccessful", groups = {"regression", "forgotPassword", "api-blocklens-users"})
    public void emailDoesNotExistInDatabase() {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput("testngocng@gmail.com"));
        resetPasswordSteps.then_verifyResponseUnsuccessful(MESSAGE_EMAIL_NOT_REGISTERED, APIError.EMAIL_NOT_REGISTERED);
    }
    @Test(description = "Forgot password unsuccessful", groups = {"regression", "forgotPassword", "api-blocklens-users"})
    public void emailFieldAreEmpty() {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(""));
        resetPasswordSteps.then_verifyResponseUnsuccessful(APIErrorMessage.EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }
    @Test(description = "Forgot password unsuccessful", groups = {"regression", "forgotPassword", "api-blocklens-users"})
    public void emailFieldAreSpace() {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput("    "));
        resetPasswordSteps.then_verifyResponseUnsuccessful(APIErrorMessage.EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }
    @Test(description = "Forgot password unsuccessful", groups = {"regression", "forgotPassword", "api-blocklens-users"})
    public void emailFieldAreMissing() {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(null));
        resetPasswordSteps.then_verifyUnsuccessful(APIError.BAD_REQUEST);
    }
    @Test(description = "Forgot password unsuccessful", groups = {"regression", "forgotPassword", "api-blocklens-users"}, dataProvider = "incorrect_email")
    public void emailIsIncorrectFormat(String emails) {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(emails));
        resetPasswordSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }
    @Test(description = "Reset password unsuccessful", groups = {"regression", "resetPassword", "api-blocklens-users", "browser"})
    public void checkTheMinimumLengthOfTheNewPasswordField() throws Exception {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(email));
        resetPasswordSteps.then_verifyForgotPassWordSuccessful(MESSAGE_OK);
        EmailLoginPage emailLoginPage = PageFactoryManager.get(EmailLoginPage.class);
        emailLoginPage
                .openGmailPage()
                .isOnEmailLoginPage(NAME_TEXT_BOX_EMAIL_ADDRESS)
                .inputDataInTextBox("testbinh25@gmail.com", NAME_TEXT_BOX_EMAIL_ADDRESS)
                .clickSignInNextBtn()
                .isOnEmailLoginPage(NAME_TEXT_BOX_PASS)
                .inputDataInTextBox("Ngoc@1997", NAME_TEXT_BOX_PASS)
                .clickPassNextBtn()
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .openLatestUnreadEmail(TITLE_EMAIL)
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
        EmailDetailPage emailDetailPage = new EmailDetailPage();
        String token = emailDetailPage.getToken(46);
        //Reset Password
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput(getRandomString(7), token));
        resetPasswordSteps.then_verifyResponseUnsuccessful(PASSWORD_MINIMUM_LENGTH, APIError.BAD_REQUEST);
        //Delete email
        emailDetailPage
                .clickInboxButton()
                .rightClickOnTheTitleEmail(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .clickDeleteOption(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
    }

    @Test(description = "Reset password unsuccessful", groups = {"regression", "resetPassword", "api-blocklens-users", "browser"})
    public void checkTheMaximumLengthOfTheNewPasswordField() throws Exception {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(email));
        resetPasswordSteps.then_verifyForgotPassWordSuccessful(MESSAGE_OK);
        EmailLoginPage emailLoginPage = PageFactoryManager.get(EmailLoginPage.class);
        emailLoginPage
                .openGmailPage()
                .isOnEmailLoginPage(NAME_TEXT_BOX_EMAIL_ADDRESS)
                .inputDataInTextBox("testbinh25@gmail.com", NAME_TEXT_BOX_EMAIL_ADDRESS)
                .clickSignInNextBtn()
                .isOnEmailLoginPage(NAME_TEXT_BOX_PASS)
                .inputDataInTextBox("Ngoc@1997", NAME_TEXT_BOX_PASS)
                .clickPassNextBtn()
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .openLatestUnreadEmail(TITLE_EMAIL)
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
        EmailDetailPage emailDetailPage = new EmailDetailPage();
        String token = emailDetailPage.getToken(46);
        //Reset Password
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput(getRandomString(51), token));
        resetPasswordSteps.then_verifyResponseUnsuccessful(PASSWORD_MAXIMUM_LENGTH, APIError.BAD_REQUEST);
        //Delete email
        emailDetailPage
                .clickInboxButton()
                .rightClickOnTheTitleEmail(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .clickDeleteOption(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
    }
    @Test(description = "Reset password unsuccessful", groups = {"regression", "resetPassword", "api-blocklens-users", "browser"})
    public void newPasswordFieldAreEmpty() throws Exception {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(email));
        resetPasswordSteps.then_verifyForgotPassWordSuccessful(MESSAGE_OK);
        EmailLoginPage emailLoginPage = PageFactoryManager.get(EmailLoginPage.class);
        emailLoginPage
                .openGmailPage()
                .isOnEmailLoginPage(NAME_TEXT_BOX_EMAIL_ADDRESS)
                .inputDataInTextBox("testbinh25@gmail.com", NAME_TEXT_BOX_EMAIL_ADDRESS)
                .clickSignInNextBtn()
                .isOnEmailLoginPage(NAME_TEXT_BOX_PASS)
                .inputDataInTextBox("Ngoc@1997", NAME_TEXT_BOX_PASS)
                .clickPassNextBtn()
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .openLatestUnreadEmail(TITLE_EMAIL)
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
        EmailDetailPage emailDetailPage = new EmailDetailPage();
        //Reset Password
        String token = emailDetailPage.getToken(46);
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput("", token));
        resetPasswordSteps.then_verifyResponseUnsuccessful(PASSWORD_EMPTY, APIError.BAD_REQUEST);
        //Delete email
        emailDetailPage
                .clickInboxButton()
                .rightClickOnTheTitleEmail(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .clickDeleteOption(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
    }
    @Test(description = "Reset password unsuccessful", groups = {"regression", "resetPassword", "api-blocklens-users", "browser"})
    public void newPasswordFieldAreSpace() throws Exception {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(email));
        resetPasswordSteps.then_verifyForgotPassWordSuccessful(MESSAGE_OK);
        EmailLoginPage emailLoginPage = PageFactoryManager.get(EmailLoginPage.class);
        emailLoginPage
                .openGmailPage()
                .isOnEmailLoginPage(NAME_TEXT_BOX_EMAIL_ADDRESS)
                .inputDataInTextBox("testbinh25@gmail.com", NAME_TEXT_BOX_EMAIL_ADDRESS)
                .clickSignInNextBtn()
                .isOnEmailLoginPage(NAME_TEXT_BOX_PASS)
                .inputDataInTextBox("Ngoc@1997", NAME_TEXT_BOX_PASS)
                .clickPassNextBtn()
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .openLatestUnreadEmail(TITLE_EMAIL)
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
        EmailDetailPage emailDetailPage = new EmailDetailPage();
        //Reset Password
        String token = emailDetailPage.getToken(46);
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput("    ", token));
        resetPasswordSteps.then_verifyResponseUnsuccessful(PASSWORD_SPACE, APIError.BAD_REQUEST);
        //Delete email
        emailDetailPage
                .clickInboxButton()
                .rightClickOnTheTitleEmail(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .clickDeleteOption(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
    }
    @Test(description = "Forgot password unsuccessful", groups = {"regression", "resetPassWord", "api-blocklens-users", "browser"})
    public void newPasswordFieldAreMissing() throws Exception {
        resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(email));
        resetPasswordSteps.then_verifyForgotPassWordSuccessful(MESSAGE_OK);
        EmailLoginPage emailLoginPage = PageFactoryManager.get(EmailLoginPage.class);
        emailLoginPage
                .openGmailPage()
                .isOnEmailLoginPage(NAME_TEXT_BOX_EMAIL_ADDRESS)
                .inputDataInTextBox("testbinh25@gmail.com", NAME_TEXT_BOX_EMAIL_ADDRESS)
                .clickSignInNextBtn()
                .isOnEmailLoginPage(NAME_TEXT_BOX_PASS)
                .inputDataInTextBox("Ngoc@1997", NAME_TEXT_BOX_PASS)
                .clickPassNextBtn()
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .openLatestUnreadEmail(TITLE_EMAIL)
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
        EmailDetailPage emailDetailPage = new EmailDetailPage();
        //Reset Password
        String token = emailDetailPage.getToken(46);
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput(null, token));
        resetPasswordSteps.then_verifyUnsuccessful(APIError.BAD_REQUEST);
        //Delete email
        emailDetailPage
                .clickInboxButton()
                .rightClickOnTheTitleEmail(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .clickDeleteOption(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
    }
    @Test(description = "Reset password unsuccessful", groups = {"regression", "resetPassword", "api-blocklens-users"})
    public void tokenInvalid() {
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput(newPassWork, getRandomString(46)));
        resetPasswordSteps.then_verifyResponseUnsuccessful(PASSWORD_RESET_LINK_INVALID, APIError.PASSWORD_RESET_LINK_INVALID);
    }
    @Test(description = "Reset password unsuccessful", groups = {"regression", "resetPassword", "api-blocklens-users"})
    public void tokenFieldAreEmpty() {
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput(newPassWork, ""));
        resetPasswordSteps.then_verifyResponseUnsuccessful(PASSWORD_RESET_LINK_INVALID, APIError.PASSWORD_RESET_LINK_INVALID);
    }
    @Test(description = "Reset password unsuccessful", groups = {"regression", "resetPassword", "api-blocklens-users"})
    public void tokenFieldAreSpace() {
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput(newPassWork, ""));
        resetPasswordSteps.then_verifyResponseUnsuccessful(PASSWORD_RESET_LINK_INVALID, APIError.PASSWORD_RESET_LINK_INVALID);
    }
    @Test(description = "Reset password unsuccessful", groups = {"regression", "resetPassword", "api-blocklens-users"})
    public void tokenFieldAreMissing() {
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput(newPassWork, null));
        resetPasswordSteps.then_verifyUnsuccessful(APIError.PASSWORD_RESET_LINK_INVALID);
    }
    @Test(description = "Reset password unsuccessful", groups = {"regression", "resetPassword", "api-blocklens-users", "browser"})
    public void tokenExpire() throws Exception {
        for (int i = 0; i <= 1; i++) {
            resetPasswordSteps.when_forgotPassword(new ForgotPasswordInput(email));
        }
        EmailLoginPage emailLoginPage = PageFactoryManager.get(EmailLoginPage.class);
        emailLoginPage
                .openGmailPage()
                .isOnEmailLoginPage(NAME_TEXT_BOX_EMAIL_ADDRESS)
                .inputDataInTextBox("testbinh25@gmail.com", NAME_TEXT_BOX_EMAIL_ADDRESS)
                .clickSignInNextBtn()
                .isOnEmailLoginPage(NAME_TEXT_BOX_PASS)
                .inputDataInTextBox("Ngoc@1997", NAME_TEXT_BOX_PASS)
                .clickPassNextBtn()
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .openLatestUnreadEmail(TITLE_EMAIL)
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
        //Reset password
        EmailDetailPage emailDetailPage = new EmailDetailPage();
        String token = emailDetailPage.getTokenOld(46);
        resetPasswordSteps.when_resetPassword(new ResetPasswordInput(newPassWork, token));
        resetPasswordSteps.then_verifyResponseUnsuccessful(RESET_PASSWORD_LINK_EXPIRED, APIError.RESET_PASSWORD_LINK_EXPIRED);
        //Delete email
        emailDetailPage
                .clickInboxButton()
                .rightClickOnTheTitleEmail(TITLE_DETAIL_EMAIL_RESET_PASSWORD)
                .clickDeleteOption(TITLE_DETAIL_EMAIL_RESET_PASSWORD);
    }
    @DataProvider(name = "incorrect_email")
    public Object[][] emails() {
        return new Object[][]
                {
                        {"blocklenstest4gmail.com"},
                        {"blocklenstest4@gmail"},
                        {"blocklenstest4@gmail.com123@"},
                        {"@gmail.com"},
                        {"..blocklenstest4@gmail.com"},
                        {"blocklens   test4    @gmail.com"},
                        {"    blocklenstest4@gmail.com"},
                        {"blocklenstest4@gmail.com    "},
                        {"   blocklenstest4@gmail.com    "},
                        {"blocklens$$$test4***@gmail.com"}
                };
    }
}
