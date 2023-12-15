package my.test.accountScreen;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.accountScreen.models.ApiKeyModel;
import microservices.accountScreen.models.NotificationInput;
import microservices.accountScreen.models.ProfileUserModel;
import microservices.accountScreen.steps.AccountScreenSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;

@Feature("Account screen")
public class CheckScreenInitializationTest extends BaseTest {
    private AccountScreenSteps accountScreenSteps = new AccountScreenSteps();
    private ApiKeyModel apiKeyModel;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        accountScreenSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "get information profile of user")
    public void getProfileOfUser(){
        ProfileUserModel profileUserModel = (ProfileUserModel) accountScreenSteps.when_getProfileOfUser()
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ProfileUserModel.class);
        accountScreenSteps.then_verifyResponseOfProfileUserAvailable(profileUserModel);
        accountScreenSteps.check_signInByAccountCorrect(profileUserModel);
    }
    @Test(description = "check format apikey default when user sign in success", dependsOnMethods = {"getProfileOfUser"})
    public void checkFormatApiKeyDefault(){
        apiKeyModel = (ApiKeyModel) accountScreenSteps.when_getUserApiKey()
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ApiKeyModel.class);
        accountScreenSteps.check_apiKeyOfUser(apiKeyModel);
    }
    @Test(description = "check format apiKey when user Regenerate Api Key", dependsOnMethods = {"checkFormatApiKeyDefault", "getProfileOfUser" })
    public void checkApiRegenerateByUser(){
        String apiKeyOld = apiKeyModel.getApiKey();
        apiKeyModel = (ApiKeyModel) accountScreenSteps.when_putUserApiKey()
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ApiKeyModel.class);
        accountScreenSteps.check_apiKeyOfUserRegenerate(apiKeyModel, apiKeyOld);
    }
    @Test(description = "user click turn on notification", dependsOnMethods = {"getProfileOfUser"})
    public void clickTurnOnNotification(){
        ProfileUserModel profile = (ProfileUserModel) accountScreenSteps.when_putNotification(new NotificationInput(true))
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ProfileUserModel.class);
        accountScreenSteps.then_checkValueOfNotificationTrue(profile);
        accountScreenSteps.when_putNotification(new NotificationInput(false));
    }
}
