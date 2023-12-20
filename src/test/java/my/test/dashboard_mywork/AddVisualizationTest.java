package my.test.dashboard_mywork;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardmywork.models.FindMyDashboardModel;
import microservices.dashboardmywork.steps.DashboardMyWorkSteps;
import microservices.dashboardpublic.constants.DashboardsConstants;
import microservices.dashboardpublic.models.*;
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
public class AddVisualizationTest extends BaseTest {
    private DashboardMyWorkSteps dashboardMyWorkSteps = new DashboardMyWorkSteps();
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    private DashboardsListModel.DataDashboard dashboard;
    private DashboardVisualsInput dashboardVisualsInput;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD)).validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "created dashboard successfully")
    public void createDashboard(){
        CreateDashboardInput createDashboardInput = new CreateDashboardInput(DashboardsConstants.DASHBOARD_NAME, true, new String[]{});
        dashboard = (DashboardsListModel.DataDashboard) dashboardMyWorkSteps.createDashboardEmpty(createDashboardInput).validateStatusCode(HttpURLConnection.HTTP_CREATED)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
    }
    @Test(description = "update dashboard add visualization", dependsOnMethods = {"createDashboard"})
    public void then_updateDashboard(){
        ListVisualizationModel listVisualizationModel = (ListVisualizationModel) dashboardMyWorkSteps.getListVisualization()
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListVisualizationModel.class);
        //create visualization
        OptionsUpdateDashboardInput option = new OptionsUpdateDashboardInput();
        dashboardVisualsInput = new DashboardVisualsInput(listVisualizationModel.getData()[0].getId(), option);
        List<DashboardVisualsInput> listVisuals = new ArrayList<>();
        listVisuals.add(dashboardVisualsInput);
        //create widget
        List<TextWidgetsInput> listWidgets = new ArrayList<>();
        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(DashboardsConstants.DASHBOARD_NAME, new String[]{}, listVisuals, listWidgets);

        dashboardMyWorkSteps.updateDashboard(updateDashboardInput, dashboard.getId());
    }
    @Test(description = "check dashboard add visualization successfully", dependsOnMethods = {"then_updateDashboard"})
    public void then_verifyUpdateDashboard(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        FindMyDashboardModel data = (FindMyDashboardModel) dashboardMyWorkSteps.findMyDashboard(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(FindMyDashboardModel.class);
        dashboardMyWorkSteps.verifyDashboardHaveVisualization(data, 1);
    }
    @Test(description = "add one visual new to dashboard", dependsOnMethods = {"then_verifyUpdateDashboard"})
    public void then_addOneVisualNewToDashboard(){
        ListVisualizationModel listVisualizationModel = (ListVisualizationModel) dashboardMyWorkSteps.getListVisualization()
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListVisualizationModel.class);
        //create visualization
        OptionsUpdateDashboardInput option = new OptionsUpdateDashboardInput(6,0,6,2);
        DashboardVisualsInput dashboardVisualsInput2 = new DashboardVisualsInput(listVisualizationModel.getData()[1].getId(), option);
        List<DashboardVisualsInput> listVisuals = new ArrayList<>();
        listVisuals.add(dashboardVisualsInput);
        listVisuals.add(dashboardVisualsInput2);
        //create widget
        List<TextWidgetsInput> listWidgets = new ArrayList<>();
        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(DashboardsConstants.DASHBOARD_NAME, new String[]{}, listVisuals, listWidgets);

        dashboardMyWorkSteps.updateDashboard(updateDashboardInput, dashboard.getId());
    }
    @Test(description = "check dashboard add one visualization successfully", dependsOnMethods = {"then_addOneVisualNewToDashboard"})
    public void then_verifyDashboardAddOneVisual(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        FindMyDashboardModel data = (FindMyDashboardModel) dashboardMyWorkSteps.findMyDashboard(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(FindMyDashboardModel.class);
        dashboardMyWorkSteps.verifyDashboardHaveVisualization(data, 2);
    }
    @Test(description = "delete dashboard successfully", dependsOnMethods = {"then_verifyDashboardAddOneVisual"})
    public void then_deleteDashboard(){
        dashboardMyWorkSteps.deleteDashboard(dashboard.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
}
