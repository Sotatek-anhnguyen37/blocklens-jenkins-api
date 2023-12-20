package my.test.dashboard_mywork;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardmywork.constants.DashboardMyWorkConstants;
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
public class AddTextWidgetTest extends BaseTest {
    private DashboardMyWorkSteps dashboardMyWorkSteps = new DashboardMyWorkSteps();
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    private DashboardsListModel.DataDashboard dashboard;
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
    @Test(description = "update dashboard add widget", dependsOnMethods = {"createDashboard"})
    public void then_updateDashboard(){
        //create widget
        OptionsUpdateDashboardInput option = new OptionsUpdateDashboardInput();
        TextWidgetsInput textWidgetsInput = new TextWidgetsInput(DashboardsConstants.WIDGET_NAME, option);
        List<TextWidgetsInput> listWidgets = new ArrayList<>();
        listWidgets.add(textWidgetsInput);
        //create visualization
        List<DashboardVisualsInput> listVisuals = new ArrayList<>();
        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(DashboardsConstants.DASHBOARD_NAME, new String[]{}, listVisuals, listWidgets);

        dashboardMyWorkSteps.updateDashboard(updateDashboardInput, dashboard.getId());
    }
    @Test(description = "check dashboard add widget successfully", dependsOnMethods = {"then_updateDashboard"})
    public void then_verifyUpdateDashboard(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        FindMyDashboardModel data = (FindMyDashboardModel) dashboardMyWorkSteps.findMyDashboard(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(FindMyDashboardModel.class);
        dashboardMyWorkSteps.verifyDashboardHaveWidget(data, DashboardsConstants.WIDGET_NAME);
    }
    @Test(description = "change text widget successfully", dependsOnMethods = {"then_verifyUpdateDashboard"})
    public void then_changeTextWidget(){
        OptionsUpdateDashboardInput option = new OptionsUpdateDashboardInput();
        TextWidgetsInput textWidgetsInput = new TextWidgetsInput(DashboardMyWorkConstants.TEXT_WIDGET_NEW, option);
        List<TextWidgetsInput> listWidgets = new ArrayList<>();
        listWidgets.add(textWidgetsInput);
        //create visualization
        List<DashboardVisualsInput> listVisuals = new ArrayList<>();
        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(DashboardsConstants.DASHBOARD_NAME, new String[]{}, listVisuals, listWidgets);

        dashboardMyWorkSteps.updateDashboard(updateDashboardInput, dashboard.getId());
    }
    @Test(description = "check change text widget successfully", dependsOnMethods = {"then_changeTextWidget"})
    public void then_checkChangeTextWidgetSuccessfully(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        FindMyDashboardModel data = (FindMyDashboardModel) dashboardMyWorkSteps.findMyDashboard(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(FindMyDashboardModel.class);
        dashboardMyWorkSteps.verifyDashboardHaveWidget(data, DashboardMyWorkConstants.TEXT_WIDGET_NEW);
    }
    @Test(description = "remove widget out dashboard", dependsOnMethods = {"then_checkChangeTextWidgetSuccessfully"})
    public void then_removeWidgetOutDashboard(){
        //create widget empty
        List<TextWidgetsInput> listWidgets = new ArrayList<>();
        //create visualization empty
        List<DashboardVisualsInput> listVisuals = new ArrayList<>();
        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(DashboardsConstants.DASHBOARD_NAME, new String[]{}, listVisuals, listWidgets);

        dashboardMyWorkSteps.updateDashboard(updateDashboardInput, dashboard.getId());
    }
    @Test(description = "check widget have removed out dashboard successfully", dependsOnMethods = {"then_removeWidgetOutDashboard"})
    public void then_checkWidgetHaveRemovedOutDashboardSuccessfully(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboard.getId());
        FindMyDashboardModel data = (FindMyDashboardModel) dashboardMyWorkSteps.findMyDashboard(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(FindMyDashboardModel.class);
        dashboardMyWorkSteps.checkWidgetHaveRemovedOut(data);
    }
    @Test(description = "delete dashboard successfully", dependsOnMethods = {"then_checkWidgetHaveRemovedOutDashboardSuccessfully"})
    public void then_deleteDashboard(){
        dashboardMyWorkSteps.deleteDashboard(dashboard.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
}
