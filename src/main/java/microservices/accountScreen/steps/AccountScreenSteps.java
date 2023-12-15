package microservices.accountScreen.steps;

import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import microservices.accountScreen.models.ApiKeyModel;
import microservices.accountScreen.models.ProfileUserModel;
import microservices.signin.model.SignInResponse;
import org.testng.Assert;
import util.CommonUtil;

public class AccountScreenSteps extends BaseApi {
    private SignInResponse signInResponse;
    @Step("sign in")
    public AccountScreenSteps when_signIn(Object body) {
        sendPost(Endpoint.BlocklensUsers.SIGN_IN_URL, body);
        signInResponse = (SignInResponse) saveResponseObject(SignInResponse.class);
        removeHeader("Authorization");
        setHeader("Authorization", "Bearer "+ signInResponse.getAccessToken());
        return this;
    }
    @Step("get information profile of user")
    public AccountScreenSteps when_getProfileOfUser(){
        sendGet(Endpoint.BlocklensUsers.GET_PROFILE_USER);
        return this;
    }
    @Step("verify response of profile user")
    public void then_verifyResponseOfProfileUserAvailable(ProfileUserModel profileUserModel){
        Assert.assertNotNull(profileUserModel.getId());
        Assert.assertNotNull(profileUserModel.getEmail());
        Assert.assertNotNull(profileUserModel.getFirstName());
        Assert.assertNotNull(profileUserModel.getLastName());
        Assert.assertNotNull(profileUserModel.getAvatar());
        Assert.assertNotNull(profileUserModel.getSetting().getBillingEmail());
        Assert.assertNotNull(profileUserModel.getAuthProviders());
    }
    @Step("verify detail response of profile")
    public void check_signInByAccountCorrect(ProfileUserModel profileUserModel){
        Assert.assertEquals(profileUserModel.getEmail(), signInResponse.getUser().getEmail());
        Assert.assertEquals(profileUserModel.getFirstName(), signInResponse.getUser().getFirstName());
        Assert.assertEquals(profileUserModel.getLastName(), signInResponse.getUser().getLastName());
    }
    @Step("get user api key")
    public AccountScreenSteps when_getUserApiKey(){
        sendGet(Endpoint.BlocklensUsers.USER_API_KEY);
        return this;
    }
    @Step("put user api key")
    public AccountScreenSteps when_putUserApiKey(){
        sendPut(Endpoint.BlocklensUsers.USER_API_KEY);
        return this;
    }
    @Step("check api key of user")
    public void check_apiKeyOfUser(ApiKeyModel apiKeyModel){
        Assert.assertTrue(CommonUtil.checkFormatApiKey(apiKeyModel.getApiKey()));
    }
    @Step("check api key of user after regenerate")
    public void check_apiKeyOfUserRegenerate(ApiKeyModel apiKeyModel, String apiKey){
        Assert.assertTrue(CommonUtil.checkFormatApiKey(apiKeyModel.getApiKey()));
        Assert.assertNotEquals(apiKeyModel.getApiKey(), apiKey);
    }
    @Step("user turn on notification to email")
    public AccountScreenSteps when_putNotification(Object body){
        sendPut(Endpoint.BlocklensUsers.UPDATE_NOTIFICATION_FLAG, body);
        return this;
    }
    @Step("check value of notification")
    public void then_checkValueOfNotificationTrue(ProfileUserModel profile){
        Assert.assertTrue(profile.getSetting().isNotificationEnabled());
    }
}