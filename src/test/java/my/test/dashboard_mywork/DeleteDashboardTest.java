package my.test.dashboard_mywork;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardmywork.steps.DashboardMyWorkSteps;
import microservices.dashboardpublic.constants.DashboardsConstants;
import microservices.dashboardpublic.models.CreateDashboardInput;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Dashboard My Work")
public class DeleteDashboardTest extends BaseTest {
    private DashboardMyWorkSteps dashboardMyWorkSteps = new DashboardMyWorkSteps();
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    private DashboardsListModel.DataDashboard dashboard;
    
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }    @Test(description = "create dashboard")
    public void createDashboard(){
        CreateDashboardInput createDashboardInput = new CreateDashboardInput(DashboardsConstants.DASHBOARD_NAME, true, new String[]{});
        dashboard = (DashboardsListModel.DataDashboard) dashboardMyWorkSteps.createDashboardEmpty(createDashboardInput).validateStatusCode(HttpURLConnection.HTTP_CREATED)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
    }
    @Test(description = "check dashboard have been created", dependsOnMethods = {"createDashboard"})
    public void then_checkDashboardHaveBeenCreated(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        dashboardMyWorkSteps.findMyDashboard(params).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "delete dashboard", dependsOnMethods = {"then_checkDashboardHaveBeenCreated"})
    public void then_deleteDashboard(){
        dashboardMyWorkSteps.deleteDashboard(dashboard.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "check dashboard have been deleted", dependsOnMethods = {"then_deleteDashboard"})
    public void then_checkDashboardHaveBeenDeleted(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        dashboardMyWorkSteps.findMyDashboard(params).validateStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
}
