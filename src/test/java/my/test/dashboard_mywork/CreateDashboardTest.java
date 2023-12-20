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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Feature("Dashboard My Work")
public class CreateDashboardTest extends BaseTest {
    private DashboardMyWorkSteps dashboardSteps = new DashboardMyWorkSteps();
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    private DashboardsListModel.DataDashboard dashboard;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "create dashboard empty")
    public void createDashboard(){
        String[] tags = {};
        CreateDashboardInput createDashboardInput = new CreateDashboardInput(DashboardsConstants.DASHBOARD_NAME, true, tags);
        dashboard = (DashboardsListModel.DataDashboard) dashboardSteps.createDashboardEmpty(createDashboardInput)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
    }
    @Test(description = "find dashboard after create", dependsOnMethods = {"createDashboard"})
    public void then_findDashboardAfterCreated(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        dashboardSteps.findMyDashboard(params) .validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "filter dashboard save", dependsOnMethods = {"then_findDashboardAfterCreated"})
    public void then_filterDashboardSaveById(){
        List<String> ids = new ArrayList<>();
        ids.add(dashboard.getId());
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardIds", ids);
        dashboardSteps.filterDashboardSave(params). validateResponse(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "check dashboard have not into dashboard public yet", dependsOnMethods = {"then_filterDashboardSaveById"})
    public void then_checkDashboardHaveNotIntoDashboardPublic(){
        Map<String, Object> params = new HashMap<>();
        DashboardsListModel dashboardList = (DashboardsListModel) dashboardPublicSteps.getAllPublicDashboards(params)
                .validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardPublicSteps.verifyDashboardHaveNotIntoListSaveDashboard(dashboardList, dashboard.getId());
    }
    @Test(description = "check dashboard have into dashboard my work and delete", dependsOnMethods = {"then_checkDashboardHaveNotIntoDashboardPublic"})
    public void then_checkDashboardHaveIntoDashboardMyWork(){
        Map<String, Object> params = new HashMap<>();
        DashboardsListModel dashboardsList = (DashboardsListModel) dashboardSteps.getListBrowseDashboard(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardSteps.checkDashboardHaveIntoListDashboardMyWork(dashboardsList, dashboard.getId());
        dashboardSteps.deleteDashboard(dashboard.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
}
