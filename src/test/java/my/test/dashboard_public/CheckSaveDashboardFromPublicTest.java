package my.test.dashboard_public;

import core.BaseTest;
import io.qameta.allure.Feature;

import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.models.SaveADashboardInput;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Dashboard list public")
public class CheckSaveDashboardFromPublicTest extends BaseTest {
    private DashboardPublicSteps dashboardSteps = new DashboardPublicSteps();
    private DashboardsListModel dashboard;
    private String dashboardId;

    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "get dashboard by name")
    public void getDashboardByName(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", "test_save_widget");
        dashboard = (DashboardsListModel) dashboardSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardId = dashboard.getData()[0].getId();
    }
    @Test(description = "save dashboard", dependsOnMethods = {"getDashboardByName"})
    public void then_saveDashboard(){
        SaveADashboardInput dashboardInput = new SaveADashboardInput(dashboardId);
        dashboardSteps.saveDashboard(dashboardInput).validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "check dashboard have into list dashboard save", dependsOnMethods = {"then_saveDashboard"})
    public void then_checkDashboardIsSaved(){
        Map<String, Object> params = new HashMap<>();
        DashboardsListModel dashboardsListModel = (DashboardsListModel) dashboardSteps.getListSaveDashboardSave(params).validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardSteps.verifyDashboardHaveIntoListSaveDashboard(dashboardsListModel, dashboardId);
    }
    @Test(description = "remove dashboard out list dashboard save", dependsOnMethods = {"then_checkDashboardIsSaved"})
    public void then_deleteDashboardOutListDashboardSave(){
        SaveADashboardInput dashboardInput = new SaveADashboardInput(dashboardId);
        dashboardSteps.removeSaveDashboardList(dashboardInput).validateStatusCode(HttpURLConnection.HTTP_OK);
    }


}
