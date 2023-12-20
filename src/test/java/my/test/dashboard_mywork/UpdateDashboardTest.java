package my.test.dashboard_mywork;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardmywork.steps.DashboardMyWorkSteps;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.models.UpdateDashboardInput;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.CommonUtil;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Feature("Dashboard My Work")
public class UpdateDashboardTest extends BaseTest {
    private DashboardMyWorkSteps dashboardMyWorkSteps = new DashboardMyWorkSteps();
    private DashboardsListModel dashboardsListModel = new DashboardsListModel();
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    private String dashboardId = "fTBt_hDSO0627aIMZar-P";
    private String nameDashboard;
    private String tag;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        dashboardPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "display list dashboard in my work tab")
    public void getListDashboardMyWork(){
        Map<String, Object> params = new HashMap<>();
        dashboardsListModel = (DashboardsListModel) dashboardMyWorkSteps.getListBrowseDashboard(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
    }
    @Test(description = "filter dashboard save", dependsOnMethods = {"getListDashboardMyWork"})
    public void then_filterDashboardSave(){
        List<String> listIds = dashboardMyWorkSteps.addDashboardIdIntoList(dashboardsListModel);
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardIds", listIds);
        dashboardMyWorkSteps.filterDashboardSave(params);
    }
    @Test(description = "find my dashboard", dependsOnMethods = {"then_filterDashboardSave"})
    public void then_findMyDashboard(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboardId);
        dashboardMyWorkSteps.findMyDashboard(params).validateStatusCode(HttpURLConnection.HTTP_OK);
        dashboardMyWorkSteps.filterDashboardSave(params);
    }
    @Test(description = "update name of dashboard", dependsOnMethods = {"then_findMyDashboard"})
    public void then_updateDashboardName(){
        tag = CommonUtil.getRandomString(3);
        nameDashboard = "Ducanh"+CommonUtil.getRandomString();
        UpdateDashboardInput updateDashboardInput = new UpdateDashboardInput(nameDashboard, new String[]{tag});
        dashboardPublicSteps.updateDashboard(updateDashboardInput, dashboardId);
    }
    @Test(description = "check update name dashboard correctly", dependsOnMethods = {"then_updateDashboardName"})
    public void then_checkUpdateNameDashboardCorrectly(){
        Map<String, Object> params = new HashMap<>();
        params.put("dashboardId", dashboardId);
        DashboardsListModel.DataDashboard data = (DashboardsListModel.DataDashboard) dashboardPublicSteps.findMyDashboardById(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.DataDashboard.class);
        dashboardMyWorkSteps.checkDashboard(data, nameDashboard, tag);
    }
}
