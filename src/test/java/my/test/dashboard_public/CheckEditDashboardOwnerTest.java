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
public class CheckEditDashboardOwnerTest extends BaseTest {
    private DashboardPublicSteps dashboardSteps = new DashboardPublicSteps();
    DashboardsListModel.DataDashboard data;
    private String dashboardIdOwner = "h-0q0yGJKB_6HISC0Ew4j";
    List<Integer> numberVisualAndWidget = new ArrayList<>();
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "open dashboard owner")
    public void openDashboardOwner(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboardIdOwner);
        DashboardsListModel.DataDashboard data = (DashboardsListModel.DataDashboard) dashboardSteps.findMyDashboardById(params)
                .validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
    }
    @Test( description = "check dashboard before added visualization and widget", dependsOnMethods = {"openDashboardOwner"})
    public void then_checkDashboardBeforeAdded(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboardIdOwner);
        data = (DashboardsListModel.DataDashboard) dashboardSteps.findMyDashboardById(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
        numberVisualAndWidget = dashboardSteps.verifyDashboardBeforeAddVisualizationAndWidget(data);
    }
    @Test(description = "add visualization and widget into dashboard ", dependsOnMethods = {"then_checkDashboardBeforeAdded"})
    public void then_addVisualizationAndWidgetIntoDashboard(){
        ListVisualizationModel listVisualizationModel = (ListVisualizationModel) dashboardSteps.getListVisualization()
                .saveResponseObject(ListVisualizationModel.class);
        System.out.println(listVisualizationModel);
        OptionsUpdateDashboardInput optionsVisual = new OptionsUpdateDashboardInput(6, 0, 6,2);
        List<DashboardVisualsInput> listVisuals = new ArrayList<>();
        DashboardVisualsInput dashboardVisualsInput = new DashboardVisualsInput(listVisualizationModel.getData()[1].getId(), optionsVisual);
        listVisuals.add(dashboardVisualsInput);

        OptionsUpdateDashboardInput optionWidget = new OptionsUpdateDashboardInput(0, 2, 6,2);
        List<TextWidgetsInput> listWidgets = new ArrayList<>();
        TextWidgetsInput textWidgetsInput = new TextWidgetsInput(DashboardsConstants.WIDGET_NAME, optionWidget);
        listWidgets.add(textWidgetsInput);

        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(data.getName(), new String[]{}, listVisuals, listWidgets);
        dashboardSteps.updateDashboard(updateDashboardInput, dashboardIdOwner);
    }
    @Test( description = "check dashboard after is added visualization and widget", dependsOnMethods = {"then_addVisualizationAndWidgetIntoDashboard"})
    public void then_checkDashboardAfterIsAdded(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboardIdOwner);
        DashboardsListModel.DataDashboard data = (DashboardsListModel.DataDashboard) dashboardSteps.findMyDashboardById(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
        dashboardSteps.verifyDashboardAfterAddVisualizationAndWidget(data, numberVisualAndWidget);
    }
    @Test(description = "update dashboard like before is added", dependsOnMethods = {"then_checkDashboardAfterIsAdded"})
    public void then_updateDashboardLikeBeforeAdded(){
        ListVisualizationModel listVisualizationModel = (ListVisualizationModel) dashboardSteps.getListVisualization()
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListVisualizationModel.class);
        OptionsUpdateDashboardInput options = new OptionsUpdateDashboardInput();
        DashboardVisualsInput dashboardVisualsInput = new DashboardVisualsInput(listVisualizationModel.getData()[0].getId(), options);

        List<DashboardVisualsInput> listVisuals = new ArrayList<>();
        listVisuals.add(dashboardVisualsInput);

        List<TextWidgetsInput> listWidgets = new ArrayList<>();
        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(data.getName(), new String[]{}, listVisuals, listWidgets);

        dashboardSteps.updateDashboard(updateDashboardInput, dashboardIdOwner);
    }
}
