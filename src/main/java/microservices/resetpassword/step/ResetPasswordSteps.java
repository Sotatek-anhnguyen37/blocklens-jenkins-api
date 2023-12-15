package microservices.resetpassword.step;

import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.net.HttpURLConnection;

public class ResetPasswordSteps extends BaseApi {
    @Step("Forgot password account")
    public ResetPasswordSteps when_forgotPassword(Object body) {
        sendPost(Endpoint.BlocklensUsers.FORGOT_PASS_WORK, body);
        return this;
    }
    @Step("Reset password account")
    public ResetPasswordSteps when_resetPassword(Object body) {
        sendPut(Endpoint.BlocklensUsers.RESET_PASS_WORK, body);
        return this;
    }
    @Step("Verify reset password successful")
    public ResetPasswordSteps then_verifyForgotPassWordSuccessful(String message) {
        validateResponse(HttpURLConnection.HTTP_CREATED);
        Assert.assertEquals(getJsonValue("message"), message);
        return this;
    }
    @Step("Verify reset password successful")
    public ResetPasswordSteps then_verifyResetPassWordSuccessful(String message) {
        validateResponse(HttpURLConnection.HTTP_OK);
        Assert.assertEquals(getJsonValue("message"), message);
        return this;
    }
    @Step("Verify reset password unsuccessful")
    public ResetPasswordSteps then_verifyResponseUnsuccessful(String message, String error) {
        validateResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(getJsonValue("message"), message);
        Assert.assertEquals(getJsonValue("error"), error);
        return this;
    }
    @Step("Verify signup unsuccessful")
    public ResetPasswordSteps then_verifyUnsuccessful(String error) {
        validateResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(getJsonValue("error"), error);
        return this;
    }
}
