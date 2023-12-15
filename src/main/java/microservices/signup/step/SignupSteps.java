package microservices.signup.step;

import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import microservices.signup.model.SignupResponse;
import org.testng.Assert;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class SignupSteps extends BaseApi {
    @Step("SignUp account")
    public SignupSteps when_signUp(Object body) {
        sendPost(Endpoint.BlocklensUsers.SIGN_UP_URL, body);
        return this;
    }

    @Step("Verify signup success")
    public SignupSteps then_verifyResponseSignUpSuccess(String email, String firstName, String lastName) {
        validateResponse(HttpURLConnection.HTTP_CREATED);
        SignupResponse response = getJsonAsObject(SignupResponse.class);
        Assert.assertEquals(email, response.getEmail(),"email incorrect");
        Assert.assertEquals(firstName, response.getFirstName(),"firstName incorrect");
        Assert.assertEquals(lastName, response.getLastName(),"lastName incorrect");
        return this;
    }
    @Step("Verify signup unsuccessful")
    public SignupSteps then_verifyResponseUnsuccessful(String message, String error) {
        validateResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(getJsonValue("message"), message);
        Assert.assertEquals(getJsonValue("error"), error);
        return this;
    }
    @Step("SignUp account")
    public SignupSteps when_verifyUserEmail(String token) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("token", token);
        sendGet(Endpoint.BlocklensUsers.VERIFY_EMAIL_URL, param);
        return this;
    }
    @Step("Verify signup unsuccessful")
    public SignupSteps then_verifyUnsuccessful(String error) {
        validateResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(getJsonValue("error"), error);
        return this;
    }
    @Step("Verify email successful")
    public SignupSteps then_verifyEmailSuccess(String message) {
        validateResponse(200);
        Assert.assertEquals(getJsonValue("message"), message);
        return this;
    }

}
