package my.test.dashboard_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Dashboard list public")
public class GetDashboardByTagsTest extends BaseTest {
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    @Test(description = "get list dashboard by tags successfully and check")
    public void getListDashboardByTagsSuccessfully(){
        String[] tags = {"tag1"};
        Map<String, Object> params = new HashMap<>();
        params.put("tag", tags);
        params.put("page", 1);
        params.put("limit", 20);
        dashboardPublicSteps.getAllPublicDashboards(params)
                .validateResponse(HttpURLConnection.HTTP_OK);
    }
}
