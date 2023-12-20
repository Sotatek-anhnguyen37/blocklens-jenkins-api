package my.test.signin;

import constants.APIError;
import constants.APIErrorMessage;
import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.signin.model.SignInInput;
import microservices.signin.step.SignInSteps;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static constants.APIErrorMessage.*;
import static util.CommonUtil.getRandomNumber;
import static util.CommonUtil.getRandomString;

@Feature("Sign in")
public class SignIn extends BaseTest {
    SignInSteps signInSteps = new SignInSteps();
    String email = "testbinh25+100@gmail.com";
    String password = "Ngoc@1997";
    String firstName = "ngocnguyen";
    String lastName = "ngoc";
    String emailDoesNotExit = "fake@gmail.com";
    @Test(description = "SignIn successfully", groups = {"regression", "signIn", "api-blocklens-users"})
    public void SignIn_Successfully() {
        //Login successfully
        signInSteps.when_signIn(new SignInInput(email, password));
        signInSteps.then_verifyResponseSignInSuccess(email, firstName, lastName);

    }
    @Test(description = "SignIn Unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void Verify_email_does_not_exist_in_database() {
        signInSteps.when_signIn(new SignInInput(emailDoesNotExit, password));
        signInSteps.then_verifyResponseUnsuccessful(APIErrorMessage.INVALID_EMAIL_OR_PASSWORD, APIError.INCORRECT_AUTH);

    }
    @Test(description = "SignIn Unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void Incorrect_password_is_wrong() {
        signInSteps.when_signIn(new SignInInput(email, getRandomNumber(8)));
        signInSteps.then_verifyResponseUnsuccessful(APIErrorMessage.INVALID_EMAIL_OR_PASSWORD, APIError.INCORRECT_AUTH);

    }

    @Test(description = "SignIn Unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void Incorrect_email_and_password() {
        signInSteps.when_signIn(new SignInInput(emailDoesNotExit, getRandomNumber(8)));
        signInSteps.then_verifyResponseUnsuccessful(APIErrorMessage.INVALID_EMAIL_OR_PASSWORD, APIError.INCORRECT_AUTH);

    }
    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void All_field_are_empty() {
        signInSteps.when_signIn(new SignInInput("", ""));
        signInSteps.then_verifyResponseUnsuccessful(SIGN_IN_ALL_FIELD_EMPTY, APIError.BAD_REQUEST);
    }

    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void Email_field_are_empty() {
        signInSteps.when_signIn(new SignInInput("", password));
        signInSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }

    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void Password_field_are_empty() {
        signInSteps.when_signIn(new SignInInput(email, ""));
        signInSteps.then_verifyResponseUnsuccessful(PASSWORD_EMPTY, APIError.BAD_REQUEST);
    }

    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void All_field_are_space() {
        signInSteps.when_signIn(new SignInInput("   ", "   "));
        signInSteps.then_verifyResponseUnsuccessful(SIGN_IN_ALL_FIELD_SPACES, APIError.BAD_REQUEST);
    }

    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void Email_field_are_space() {
        signInSteps.when_signIn(new SignInInput("   ", password));
        signInSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);
    }
    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void Password_field_are_space() {
        signInSteps.when_signIn(new SignInInput(email, "   "));
        signInSteps.then_verifyResponseUnsuccessful(PASSWORD_SPACE, APIError.BAD_REQUEST);
    }
    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void Check_the_minimum_length_of_the_password_field() {
        signInSteps.when_signIn(new SignInInput(email, getRandomString(7)));
        signInSteps.then_verifyResponseUnsuccessful(PASSWORD_MINIMUM_LENGTH, APIError.BAD_REQUEST);

    }
    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void Check_the_maximum_length_of_the_password_field() {
        signInSteps.when_signIn(new SignInInput(email, getRandomString(51)));
        signInSteps.then_verifyResponseUnsuccessful(PASSWORD_MAXIMUM_LENGTH, APIError.BAD_REQUEST);

    }
    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"}, dataProvider = "missing")
    public void Required_field_is_missing(String field1) {
        signInSteps.when_signIn(new SignInInput(field1));
        signInSteps.then_verifyUnsuccessful(APIError.BAD_REQUEST);

    }
    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"})
    public void All_required_field_is_missing() {
        signInSteps.when_signIn(new SignInInput());
        signInSteps.then_verifyUnsuccessful(APIError.BAD_REQUEST);

    }
    @Test(description = "SignIn unsuccessful", groups = {"regression", "signIn", "api-blocklens-users"}, dataProvider = "incorrect_email")
    public void email_is_incorrect_format(String emails) {
        signInSteps.when_signIn(new SignInInput(emails, password));
        signInSteps.then_verifyResponseUnsuccessful(EMAIL_INCORRECT, APIError.BAD_REQUEST);

    }

    @DataProvider(name = "missing")
    public Object[][] missing() {
        return new Object[][]
                {
                        {password},
                        {email}
                };
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
