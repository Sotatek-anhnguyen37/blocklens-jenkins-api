package my.test.signup;

import constants.APIError;
import constants.APIErrorMessage;
import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.email.EmailDetailPage;
import microservices.email.EmailLoginPage;
import microservices.resendemail.model.ResendInput;
import microservices.resendemail.step.ResendSteps;
import microservices.signin.model.SignInInput;
import microservices.signin.step.SignInSteps;
import microservices.signup.model.SignupInput;
import microservices.signup.step.SignupSteps;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import web_core.PageFactoryManager;
import web_core.WebAppDriverManager;

import static constants.APIErrorMessage.*;
import static constants.EventsUfpConstants.*;
import static util.CommonUtil.getRandomNumber;
import static util.CommonUtil.getRandomString;

@Feature("api-blocklens-users")
public class SignUp extends BaseTest {
    SignupSteps signupSteps = new SignupSteps();
    SignInSteps signInSteps = new SignInSteps();
    ResendSteps resendSteps = new ResendSteps();

    String email = "testbinh25+" + getRandomNumber(5) + "@gmail.com";
    String password = "Ngoc@114";
    String firstName = "Ngoc";
    String lastName = "Nguyen";
    @BeforeMethod(alwaysRun = true, onlyForGroups = {"browser"})
    public void beforeMethod() {
        WebAppDriverManager.openMultiBrowser("chrome");
    }

    @AfterMethod(alwaysRun = true, onlyForGroups = {"browser"})
    public void closeBrowser() {
        WebAppDriverManager.closeBrowserAndDriver(WebAppDriverManager.getDriver());
    }

    @Test(description = "This test case is SignUp successfully and then verify the token once it has been successfully registered", groups = {"regression", "signup", "api-blocklens-users", "browser"})
    public void SignUp_Successfully_and_verify_the_token_once_it_has_been_successfully_registered() throws Exception {
        //Get token in email
        signupSteps.when_signUp(new SignupInput(email, password, firstName, lastName));
        signupSteps.then_verifyResponseSignUpSuccess(email, firstName, lastName);
        EmailLoginPage emailLoginPage = PageFactoryManager.get(EmailLoginPage.class);
        emailLoginPage
                .openGmailPage()
                .isOnEmailLoginPage(NAME_TEXT_BOX_EMAIL_ADDRESS)
                .inputDataInTextBox("testbinh25@gmail.com", NAME_TEXT_BOX_EMAIL_ADDRESS)
                .clickSignInNextBtn()
                .isOnEmailLoginPage(NAME_TEXT_BOX_PASS)
                .inputDataInTextBox("Ngoc@1997", NAME_TEXT_BOX_PASS)
                .clickPassNextBtn()
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL)
                .openLatestUnreadEmail(TITLE_EMAIL)
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL);
        EmailDetailPage emailDetailPage = new EmailDetailPage();
        String token = emailDetailPage.getToken(44);
        //Verify token success
        signupSteps.when_verifyUserEmail(token);
        signupSteps.then_verifyEmailSuccess(MESSAGE_OK);

        //Verify the token once it has been successfully registered
        signupSteps.when_verifyUserEmail(token);
        signupSteps.then_verifyResponseUnsuccessful(LINK_INVALID, APIError.VERIFY_LINK_INVALID);

        //Delete email
        emailDetailPage
                .clickInboxButton()
                .rightClickOnTheTitleEmail(TITLE_DETAIL_EMAIL)
                .clickDeleteOption(TITLE_DETAIL_EMAIL);
    }

    @Test(description = "SignUp Unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Input_your_registered_email_to_register_into_the_system() {
        signupSteps.when_signUp(new SignupInput("testbinh25+100@gmail.com", password, firstName, lastName));
        signupSteps.then_verifyResponseUnsuccessful(APIErrorMessage.EMAIL_REGISTERED, APIError.EMAIL_EXISTED);
    }

    @Test(description = "SignUp Unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Input_token_that_does_not_exist() {
        signupSteps.when_verifyUserEmail(getRandomString(10));
        signupSteps.then_verifyResponseUnsuccessful(APIErrorMessage.LINK_INVALID, APIError.VERIFY_LINK_INVALID);
    }

    @Test(description = "SignUp Unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void User_login_with_the_email_he_just_signed_up_but_has_not_verified_the_email_yet() {
        signupSteps.when_signUp(new SignupInput(email, password, firstName, lastName));
        signupSteps.then_verifyResponseSignUpSuccess(email, firstName, lastName);
        signInSteps.when_signIn(new SignInInput(email, password));
        signInSteps.then_verifyResponseUnsuccessful(APIErrorMessage.EMAIL_NOT_VERIFIED, APIError.EMAIL_NOT_VERIFIED);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void All_field_are_empty() {
        //Created account by api
        signupSteps.when_signUp(new SignupInput("", "", "", ""));
        signupSteps.then_verifyResponseUnsuccessful(ALL_FIELD_EMPTY, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Email_field_are_empty() {
        signupSteps.when_signUp(new SignupInput("", password, firstName, lastName));
        signupSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Password_field_are_empty() {
        signupSteps.when_signUp(new SignupInput(email, "", firstName, lastName));
        signupSteps.then_verifyResponseUnsuccessful(PASSWORD_EMPTY, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void FirstName_field_are_empty() {
        signupSteps.when_signUp(new SignupInput(email, password, "", lastName));
        signupSteps.then_verifyResponseUnsuccessful(FIRST_NAME_EMPTY, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void LastName_field_are_empty() {
        signupSteps.when_signUp(new SignupInput(email, password, firstName, ""));
        signupSteps.then_verifyResponseUnsuccessful(LAST_NAME_EMPTY, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void All_field_are_space() {
        //Created account by api
        signupSteps.when_signUp(new SignupInput("   ", "   ", "   ", "   "));
        signupSteps.then_verifyResponseUnsuccessful(ALL_FIELD_SPACES, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Email_field_are_space() {
        signupSteps.when_signUp(new SignupInput("   ", password, firstName, lastName));
        signupSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Password_field_are_space() {
        signupSteps.when_signUp(new SignupInput(email, "    ", firstName, lastName));
        signupSteps.then_verifyResponseUnsuccessful(PASSWORD_SPACE, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void FirstName_field_are_space() {
        signupSteps.when_signUp(new SignupInput(email, password, "   ", lastName));
        signupSteps.then_verifyResponseUnsuccessful(FIRST_NAME_SPACE, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void LastName_field_are_space() {
        signupSteps.when_signUp(new SignupInput(email, password, firstName, "   "));
        signupSteps.then_verifyResponseUnsuccessful(LAST_NAME_SPACE, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Check_the_minimum_length_of_the_password_field() {
        signupSteps.when_signUp(new SignupInput(email, getRandomString(7), firstName, lastName));
        signupSteps.then_verifyResponseUnsuccessful(PASSWORD_MINIMUM_LENGTH, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Check_the_maximum_length_of_the_password_field() {
        signupSteps.when_signUp(new SignupInput(email, getRandomString(51), firstName, lastName));
        signupSteps.then_verifyResponseUnsuccessful(PASSWORD_MAXIMUM_LENGTH, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Check_the_maximum_length_of_the_firstName_field() {
        signupSteps.when_signUp(new SignupInput(email, password, getRandomString(101), lastName));
        signupSteps.then_verifyResponseUnsuccessful(FIRST_NAME_MAXIMUM_LENGTH, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Check_the_maximum_length_of_the_lastName_field() {
        signupSteps.when_signUp(new SignupInput(email, password, firstName, getRandomString(101)));
        signupSteps.then_verifyResponseUnsuccessful(LAST_NAME_MAXIMUM_LENGTH, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"}, dataProvider = "incorrect_email")
    public void email_is_incorrect_format(String emails) {
        signupSteps.when_signUp(new SignupInput(emails, password, firstName, lastName));
        signupSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"}, dataProvider = "missing")
    public void Required_field_is_missing(String field1, String field2, String field3) {
        signupSteps.when_signUp(new SignupInput(field1, field2, field3));
        signupSteps.then_verifyUnsuccessful(APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void All_required_field_is_missing() {
        signupSteps.when_signUp(new SignupInput());
        signupSteps.then_verifyUnsuccessful(APIError.BAD_REQUEST);
    }

    @Test(description = "SignUp Unsuccessful", groups = {"regression", "signup", "api-blocklens-users", "browser"})
    public void Click_the_Resend_button_many_times_and_verify_email_does_not_have_to_send_the_latest_confirmation() throws Exception {
        signupSteps.when_signUp(new SignupInput(email, password, firstName, lastName));
        signupSteps.then_verifyResponseSignUpSuccess(email, firstName, lastName);
        resendSteps.when_resendEmail(new ResendInput(email));
        resendSteps.then_verifyResponseResendSuccess(MESSAGE_OK);
        EmailLoginPage emailLoginPage = PageFactoryManager.get(EmailLoginPage.class);
        //Get token in email
        emailLoginPage
                .openGmailPage()
                .isOnEmailLoginPage(NAME_TEXT_BOX_EMAIL_ADDRESS)
                .inputDataInTextBox("testbinh25@gmail.com", NAME_TEXT_BOX_EMAIL_ADDRESS)
                .clickSignInNextBtn()
                .isOnEmailLoginPage(NAME_TEXT_BOX_PASS)
                .inputDataInTextBox("Ngoc@1997", NAME_TEXT_BOX_PASS)
                .clickPassNextBtn()
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL)
                .openLatestUnreadEmail(TITLE_EMAIL)
                .isOnEmailMainPage(TITLE_DETAIL_EMAIL);
        EmailDetailPage emailDetailPage = new EmailDetailPage();
        String token = emailDetailPage.getTokenOld(44);

        //Verify the token once it has been successfully registered
        signupSteps.when_verifyUserEmail(token);
        signupSteps.then_verifyResponseUnsuccessful(APIErrorMessage.LINK_INVALID, APIError.VERIFY_LINK_INVALID);

        signInSteps.when_signIn(new SignInInput(email, password));
        signInSteps.then_verifyResponseUnsuccessful(APIErrorMessage.EMAIL_NOT_VERIFIED, APIError.EMAIL_NOT_VERIFIED);

        //Delete email
        emailDetailPage
                .clickInboxButton()
                .rightClickOnTheTitleEmail(TITLE_DETAIL_EMAIL)
                .clickDeleteOption(TITLE_DETAIL_EMAIL);
    }

    @Test(description = "Resend unsuccessful", groups = {"regression", "signup", "api-blocklens-users"}, dataProvider = "incorrect_email")
    public void Email_field_is_incorrect_format(String emails) {
        resendSteps.when_resendEmail(new ResendInput(emails));
        resendSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }

    @Test(description = "Resend unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Email_empty() {
        resendSteps.when_resendEmail(new ResendInput(""));
        resendSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }
    @Test(description = "Resend unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Email_space() {
        resendSteps.when_resendEmail(new ResendInput("    "));
        resendSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }

    @Test(description = "Resend unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Email_field_is_missing() {
        resendSteps.when_resendEmail(new ResendInput());
        resendSteps.then_verifyUnsuccessful(APIError.BAD_REQUEST);
    }
    @Test(description = "Resend unsuccessful", groups = {"regression", "signup", "api-blocklens-users"})
    public void Email_is_not_registered() {
        resendSteps.when_resendEmail(new ResendInput(email));
        resendSteps.then_verifyResponseUnsuccessful(MESSAGE_EMAIL_NOT_REGISTERED, APIError.EMAIL_NOT_REGISTERED);
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

    @DataProvider(name = "missing")
    public Object[][] missing() {
        return new Object[][]
                {
                        {password, firstName, lastName},
                        {email, firstName, lastName},
                        {email, password, lastName},
                        {email, password, firstName}
                };
    }
}

