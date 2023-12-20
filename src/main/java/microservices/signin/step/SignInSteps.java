package microservices.signin.step;

import constants.Endpoint;
import constants.Url;
import core.BaseApi;
import io.qameta.allure.Step;
import microservices.signin.model.SignInResponse;
import org.testng.Assert;

import java.net.HttpURLConnection;

public class SignInSteps extends BaseApi {
    @Step("SignIn account")
    public SignInSteps when_signIn(Object body) {
        sendPost(Endpoint.BlocklensUsers.SIGN_IN_URL, body);
        return this;
    }
    @Step("Verify signup success")
    public SignInSteps then_verifyResponseSignInSuccess(String email, String firstName, String lastName) {
        validateResponse(HttpURLConnection.HTTP_CREATED);
        SignInResponse response = getJsonAsObject(SignInResponse.class);
        Assert.assertEquals(email, response.getUser().getEmail(),"email incorrect");
        Assert.assertEquals(firstName, response.getUser().getFirstName(),"firstName incorrect");
        Assert.assertEquals(lastName, response.getUser().getLastName(),"lastName incorrect");
        return this;
    }

    @Step("Verify signup unsuccessful")
    public SignInSteps then_verifyResponseUnsuccessful(String message, String error) {
        validateResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(getJsonValue("message"), message);
        Assert.assertEquals(getJsonValue("error"), error);
        return this;
    }
    @Step("Verify signup unsuccessful")
    public SignInSteps then_verifyUnsuccessful(String error) {
        validateResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(getJsonValue("error"), error);
        return this;
    }
}
