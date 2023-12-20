package my.test.dashboard_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.models.ErrorMessageModel;
import microservices.dashboardpublic.models.TagsModel;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Dashboard list public")
public class GetDashboardBySearchBoxTest extends BaseTest {
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    @Test(description = "check button filter with list dashboard public")
    public void checkButtonFilter(){
        TagsModel tagsModel = (TagsModel)dashboardPublicSteps.getTrendingDashboardTags()
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(TagsModel.class);
        dashboardPublicSteps.verifyResponseOfTrendingDashboardTags(tagsModel);
    }
    @Test(description = "check search box of list dashboard public with name exists", dataProvider = "paramsSearchName")
    public void checkSearchBoxOfDashboardsPublicWithNameExists(Object search, Object page, Object limit){
        Map<String, Object> params = new HashMap<>();
        params.put("search", search);
        params.put("page", page);
        params.put("limit", limit);
        DashboardsListModel dashboardsListModel = (DashboardsListModel) dashboardPublicSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardPublicSteps.checkListDashboardHasSearchName(dashboardsListModel, search);
    }
    @DataProvider (name = "paramsSearchName")
    public Object[][] dataSearchName(){
        return new Object[][]{
                {"ducanh", 1, 20},
                {"  ducanh  ", 1, 20}
        };
    }
    @Test(description = "check search box of list dashboard public unsuccessfully", dataProvider = "paramsEmptyAndSpace")
    public void checkSearchBoxOfDashboardsPublicUnsuccessfully(Object search , Object page, Object limit){
        Map<String, Object> params = new HashMap<>();
        params.put("search", search);
        params.put("page", page);
        params.put("limit", limit);
        ErrorMessageModel errorMessageModel = (ErrorMessageModel) dashboardPublicSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_BAD_REQUEST)
                .saveResponseObject(ErrorMessageModel.class);
        dashboardPublicSteps.verifyResponseBadRequestListDashboardParamsEmpty(errorMessageModel);
    }
    @DataProvider(name = "paramsEmptyAndSpace")
    public Object[][] dataSearchDashboard(){
        return new Object[][]{
                {"", 1, 20},
                {"   ", 1, 20},
        };
    }
}
