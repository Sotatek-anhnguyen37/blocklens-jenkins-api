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
public class CheckConditionDisplayDashboardPublicWhenAddWidgetAndVisualTest extends BaseTest {
    private DashboardPublicSteps dashboardStep = new DashboardPublicSteps();
    private DashboardsListModel.DataDashboard dashboard;

    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardStep.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "check dashboard have not in system yet")
    public void checkDashboardHaveNotInSystemYet(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", DashboardsConstants.DASHBOARD_NAME);
        DashboardsListModel dashboardsListModel = (DashboardsListModel) dashboardStep.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardStep.verifyListDashboardsEmpty(dashboardsListModel);
    }
    @Test(description = "create empty dashboard", dependsOnMethods = {"checkDashboardHaveNotInSystemYet"})
    public void then_createDashboardHaveNotVisualization(){
        String[] tags = {};
        CreateDashboardInput createDashboardInput = new CreateDashboardInput(DashboardsConstants.DASHBOARD_NAME_WIDGET, true, tags);
        dashboard = (DashboardsListModel.DataDashboard) dashboardStep.createDashboardEmpty(createDashboardInput)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
    }
    @Test(description = "verify dashboard newly created have not widget and visualization", dependsOnMethods = {"then_createDashboardHaveNotVisualization"})
    public void then_verifyDashboardNewlyCreatedHaveNotWidgetAndVisual(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        DashboardsListModel.DataDashboard data = (DashboardsListModel.DataDashboard) dashboardStep.findMyDashboardById(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
        dashboardStep.verifyDashboardHaveNotWidgetAndVisual(data);
    }
    @Test(description = "add visualization and widget into dashboard ", dependsOnMethods = {"then_verifyDashboardNewlyCreatedHaveNotWidgetAndVisual"})
    public void then_addVisualizationAndWidgetIntoDashboard(){
        ListVisualizationModel listVisualizationModel = (ListVisualizationModel) dashboardStep.getListVisualization()
                .saveResponseObject(ListVisualizationModel.class);

        OptionsUpdateDashboardInput optionsVisual = new OptionsUpdateDashboardInput();
        List<DashboardVisualsInput> listVisuals = new ArrayList<>();
        DashboardVisualsInput dashboardVisualsInput = new DashboardVisualsInput(listVisualizationModel.getData()[0].getId(), optionsVisual);
        listVisuals.add(dashboardVisualsInput);

        OptionsUpdateDashboardInput optionWidget = new OptionsUpdateDashboardInput(6, 0, 6,2);
        List<TextWidgetsInput> listWidgets = new ArrayList<>();
        TextWidgetsInput textWidgetsInput = new TextWidgetsInput(DashboardsConstants.WIDGET_NAME, optionWidget);
        listWidgets.add(textWidgetsInput);

        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(DashboardsConstants.DASHBOARD_NAME_WIDGET, new String[]{}, listVisuals, listWidgets);
        dashboardStep.updateDashboard(updateDashboardInput, dashboard.getId());
    }
    @Test( description = "check dashboard is already on the dashboard public after added widget and visualization", dependsOnMethods = {"then_addVisualizationAndWidgetIntoDashboard"})
    public void then_checkDashboardAlreadyDisplayOnPublic(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        DashboardsListModel.DataDashboard data = (DashboardsListModel.DataDashboard) dashboardStep.findMyDashboardById(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
        dashboardStep.verifyDashboardHaveWidgetAndVisual(data);
    }
    @Test(description = "delete dashboard newly created", dependsOnMethods = {"then_addVisualizationAndWidgetIntoDashboard"})
    public void then_deleteDashboardNewlyCreated(){
        dashboardStep.deleteOneDashboard(dashboard.getId());
    }
}
