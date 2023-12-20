package my.test.dashboard_mywork;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardmywork.steps.DashboardMyWorkSteps;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.models.SaveADashboardInput;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Dashboard My Work")
public class SaveDashboardTest extends BaseTest {
    private DashboardMyWorkSteps myWorkSteps = new DashboardMyWorkSteps();
    private DashboardPublicSteps dashboardSteps = new DashboardPublicSteps();
    private DashboardsListModel dashboard;
    private String dashboardId;
    private String nameDashboard = "test_defalt_info123";

    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "get dashboard by name")
    public void getMyDashboardByName(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", nameDashboard);
        dashboard = (DashboardsListModel) myWorkSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardId = dashboard.getData()[0].getId();
    }
    @Test(description = "save dashboard", dependsOnMethods = {"getMyDashboardByName"})
    public void then_saveDashboard(){
        SaveADashboardInput dashboardInput = new SaveADashboardInput(dashboardId);
        myWorkSteps.saveDashboard(dashboardInput).validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "check dashboard have into list dashboard save", dependsOnMethods = {"then_saveDashboard"})
    public void then_checkDashboardIsSaved(){
        Map<String, Object> params = new HashMap<>();
        DashboardsListModel dashboardsListModel = (DashboardsListModel) myWorkSteps.getListDashboardSave(params).validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        myWorkSteps.verifyDashboardHaveIntoListSaveDashboard(dashboardsListModel, dashboardId);
    }
    @Test(description = "remove dashboard out list dashboard save", dependsOnMethods = {"then_checkDashboardIsSaved"})
    public void then_deleteDashboardOutListDashboardSave(){
        SaveADashboardInput dashboardInput = new SaveADashboardInput(dashboardId);
        myWorkSteps.removeSaveDashboardList(dashboardInput).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "check dashboard have not into list dashboard save", dependsOnMethods = {"then_deleteDashboardOutListDashboardSave"})
    public void then_checkDashboardIsUnsaved(){
        Map<String, Object> params = new HashMap<>();
        DashboardsListModel dashboardsListModel = (DashboardsListModel) myWorkSteps.getListDashboardSave(params).validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        myWorkSteps.verifyDashboardHaveNotIntoListSaveDashboard(dashboardsListModel, dashboardId);
    }
}
