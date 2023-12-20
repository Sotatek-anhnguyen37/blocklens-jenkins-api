package my.test.query_dashboard_saved;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import microservices.querypublic.models.ListQueryPublicModel;
import microservices.querypublic.steps.QueryPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Query/Dashboard Saved")
public class CheckSearchBoxTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private DashboardPublicSteps dashboardPublicSteps = new DashboardPublicSteps();
    private DashboardsListModel dashboardsListModel;
    private ListQueryPublicModel listQueryPublicModel;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "get list dashboard save by name and check")
    public void getListDashboardSaveByName(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", "anh");
        dashboardsListModel = (DashboardsListModel) dashboardPublicSteps.getListSaveDashboardSave(params).validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardPublicSteps.checkListDashboardHasSearchName(dashboardsListModel, "anh");
    }

    @Test(description = "get list dashboard save by name not exist and check")
    public void getListDashboardSaveNameNotExist(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", "@#$%");
        dashboardsListModel = (DashboardsListModel) dashboardPublicSteps.getListSaveDashboardSave(params).validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        dashboardPublicSteps.verifyListDashboardsEmpty(dashboardsListModel);
    }

    @Test(description = "get list query save by name and check")
    public void getListQuerySaveByName(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", "anh");
        listQueryPublicModel = (ListQueryPublicModel) queryPublicSteps.getListQueriesSaved(params).validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryPublicSteps.checkListQuerySearchedByName(listQueryPublicModel, "anh");
    }

    @Test(description = "get list query save by name not exist and check")
    public void getListQuerySaveByNameNotExist(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", "@#$%");
        listQueryPublicModel = (ListQueryPublicModel) queryPublicSteps.getListQueriesSaved(params).validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryPublicSteps.verifyNameQueryAfterSearchNotExist(listQueryPublicModel);
    }
}
