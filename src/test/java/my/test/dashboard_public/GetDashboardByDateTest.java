package my.test.dashboard_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.models.ErrorResponseModel;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Dashboard list public")
public class GetDashboardByDateTest extends BaseTest {
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    @Test(description = "check list dashboard when click choose Date low to high")
    public void checkListDashboardWhenClickChooseDateLowToHigh(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:asc");
        DashboardsListModel dashboardsListModel = (DashboardsListModel) dashboardPublicSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardPublicSteps.checkListDashboardsSortedByCreatedAt(dashboardsListModel, params.get("orderBy").toString());
    }
    @Test(description = "check list dashboard when click choose Date high to low")
    public void checkListDashboardWhenClickChooseDateHighToLow(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:desc");
        DashboardsListModel dashboardsListModel = (DashboardsListModel) dashboardPublicSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardPublicSteps.checkListDashboardsSortedByCreatedAt(dashboardsListModel, params.get("orderBy").toString());
    }
    @Test(description = "check list dashboard when sorted by updateAt", dataProvider = "paramsUpdateAt")
    public void checkListDashboardsWhenSortedByUpdateAt(String orderBy){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", orderBy);
        ErrorResponseModel errorResponseModel = (ErrorResponseModel) dashboardPublicSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_BAD_REQUEST)
                .saveResponseObject(ErrorResponseModel.class);
        dashboardPublicSteps.verifyResponseErrorSortedByUpdateAt(errorResponseModel);
    }
    @DataProvider(name = "paramsUpdateAt")
    public Object[][] dataOrderByUpdateAt(){
        return new Object[][]{
                {"update_at:desc"},
                {"update_at:asc"},
        };
    }
    @Test(description = "check list dashboard when sorted by invalid orderBy", dataProvider = "paramsInvalid")
    public void checkListDashboardsWhenSortedByOrderByInvalid(String orderBy){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", orderBy);
        ErrorResponseModel errorResponseModel = (ErrorResponseModel) dashboardPublicSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_BAD_REQUEST)
                .saveResponseObject(ErrorResponseModel.class);
        dashboardPublicSteps.verifyResponseErrorSortedByUpdateAt(errorResponseModel);
    }
    @DataProvider(name = "paramsInvalid")
    public Object[][] dataOrderByInvalid(){
        return new Object[][]{
                {"1234"},
                {"abcd"},
        };
    }
}
