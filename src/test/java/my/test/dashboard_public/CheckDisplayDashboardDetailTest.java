package my.test.dashboard_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Dashboard list public")
public class CheckDisplayDashboardDetailTest extends BaseTest {
    private DashboardPublicSteps dashboardSteps = new DashboardPublicSteps();
    private String dashboardIdNotOwner = "NPQ8AiIwmgvlazwvjTRt_";
    private String dashboardIdOwner = "h-0q0yGJKB_6HISC0Ew4j";
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "check show dashboard is not owner")
    public void checkShowDashboardIsNotOwner(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboardIdNotOwner);
        dashboardSteps.findMyDashboardById(params).validateStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);

        DashboardsListModel.DataDashboard data = (DashboardsListModel.DataDashboard)dashboardSteps.getDashboardDetail(dashboardIdNotOwner)
                .validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
        dashboardSteps.verifyDashboardIsNotOwner(data);
    }
    @Test(description = "check show dashboard is owner")
    public void checkShowDashboardIsOwner(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboardIdOwner);
        DashboardsListModel.DataDashboard data = (DashboardsListModel.DataDashboard) dashboardSteps.findMyDashboardById(params)
                .validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
        dashboardSteps.verifyDashboardIsOwner(data);
    }
}
