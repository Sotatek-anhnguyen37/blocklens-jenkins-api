package microservices.resendemail.step;

import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.net.HttpURLConnection;

public class ResendSteps extends BaseApi {
    @Step("Resend account")
    public ResendSteps when_resendEmail(Object body) {
        sendPost(Endpoint.BlocklensUsers.RESEND_EMAIL_URL, body);
        return this;
    }

    @Step("Verify resend success")
    public ResendSteps then_verifyResponseResendSuccess(String message) {
        validateResponse(HttpURLConnection.HTTP_CREATED);
        Assert.assertEquals(getJsonValue("message"), message);
        return this;
    }
    @Step("Verify resend unsuccessful")
    public ResendSteps then_verifyResponseUnsuccessful(String message, String error) {
        validateResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(getJsonValue("message"), message);
        Assert.assertEquals(getJsonValue("error"), error);
        return this;
    }
    @Step("Verify resend unsuccessful")
    public ResendSteps then_verifyUnsuccessful(String error) {
        validateResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(getJsonValue("error"), error);
        return this;
    }
}
