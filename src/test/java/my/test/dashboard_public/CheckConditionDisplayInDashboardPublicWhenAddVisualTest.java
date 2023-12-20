package my.test.dashboard_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
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

@Feature("Dashboard list public")
public class CheckConditionDisplayInDashboardPublicWhenAddVisualTest extends BaseTest {
    private DashboardPublicSteps dashboardSteps = new DashboardPublicSteps();
    private DashboardsListModel.DataDashboard dashboard;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "check dashboard have not in system yet")
    public void checkDashboardHaveNotInSystemYet(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", DashboardsConstants.DASHBOARD_NAME_VISUAL);
        DashboardsListModel dashboardsListModel = (DashboardsListModel) dashboardSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardSteps.verifyListDashboardsEmpty(dashboardsListModel);
    }
    @Test(description = "create empty dashboard have not visualization yet", dependsOnMethods = {"checkDashboardHaveNotInSystemYet"})
    public void then_createDashboardHaveNotVisualization(){
        String[] tags = {};
        CreateDashboardInput createDashboardInput = new CreateDashboardInput(DashboardsConstants.DASHBOARD_NAME_VISUAL, true, tags);
        dashboard = (DashboardsListModel.DataDashboard) dashboardSteps.createDashboardEmpty(createDashboardInput)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
    }
    @Test(description = "verify dashboard newly created have not visualization", dependsOnMethods = {"then_createDashboardHaveNotVisualization"})
    public void then_verifyDashboardNewlyCreatedHaveNotVisualization(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        DashboardsListModel.DataDashboard data = (DashboardsListModel.DataDashboard) dashboardSteps.findMyDashboardById(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
        dashboardSteps.verifyDashboardHaveNotWidgetAndVisual(data);
    }
    @Test(description = "add visualization into dashboard ", dependsOnMethods = {"then_verifyDashboardNewlyCreatedHaveNotVisualization"})
    public void then_addVisualizationIntoDashboard(){
        ListVisualizationModel listVisualizationModel = (ListVisualizationModel) dashboardSteps.getListVisualization()
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListVisualizationModel.class);
        OptionsUpdateDashboardInput options = new OptionsUpdateDashboardInput();
        DashboardVisualsInput dashboardVisualsInput = new DashboardVisualsInput(listVisualizationModel.getData()[0].getId(), options);

        List<DashboardVisualsInput> listVisuals = new ArrayList<>();
        listVisuals.add(dashboardVisualsInput);

        List<TextWidgetsInput> listWidgets = new ArrayList<>();
        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(DashboardsConstants.DASHBOARD_NAME_VISUAL, new String[]{}, listVisuals, listWidgets);

        dashboardSteps.updateDashboard(updateDashboardInput, dashboard.getId());
    }
    @Test( description = "check dashboard is already on the dashboard public after added visualization", dependsOnMethods = {"then_addVisualizationIntoDashboard"})
    public void then_checkDashboardAlreadyDisplayOnPublic(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        DashboardsListModel.DataDashboard data = (DashboardsListModel.DataDashboard) dashboardSteps.findMyDashboardById(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
        dashboardSteps.verifyDashboardHaveVisualization(data);
    }
    @Test(description = "delete dashboard newly created", dependsOnMethods = {"then_addVisualizationIntoDashboard"})
    public void then_deleteDashboardNewlyCreated(){
        dashboardSteps.deleteOneDashboard(dashboard.getId());
    }
}
