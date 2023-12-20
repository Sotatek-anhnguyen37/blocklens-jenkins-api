package my.test.dashboard_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.models.ErrorMessageModel;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Dashboard list public")
public class ListDashboardPublicTest extends BaseTest {
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    @Test(description = "get all public dashboards successfully")
    public void getAllPublicDashboardSuccessfully(){
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 20);
        DashboardsListModel dashboardsListModel = (DashboardsListModel) dashboardPublicSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardPublicSteps.verifyResponseListDashboard(dashboardsListModel, params);
    }
    @Test(description = "get all public dashboards bad request", dataProvider = "paramsDashboard")
    public void getAllPublicDashboardsBadRequest(Object page, Object limit){
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("limit", limit);
        ErrorMessageModel errorMessageModel = (ErrorMessageModel) dashboardPublicSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_BAD_REQUEST)
                .saveResponseObject(ErrorMessageModel.class);
        dashboardPublicSteps.verifyResponseBadRequestListDashboard(errorMessageModel);
    }

    @DataProvider(name = "paramsDashboard")
    public Object[][] dataDashboard(){
        return new Object[][]{
                {"", 20},
                {1, ""},
                {"", ""},
                {"  ", 20},
                {1, "  "},
                {"  ", "  "}
        };
    }
}
