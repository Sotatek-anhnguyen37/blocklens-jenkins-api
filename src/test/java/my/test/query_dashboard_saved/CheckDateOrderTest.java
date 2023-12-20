package my.test.query_dashboard_saved;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardmywork.steps.DashboardMyWorkSteps;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.querypublic.models.ListQueryPublicModel;
import microservices.querypublic.steps.QueryPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Query/Dashboard Saved")
public class CheckDateOrderTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private DashboardMyWorkSteps dashboardMyWorkSteps = new DashboardMyWorkSteps();
    private DashboardsListModel dashboardsListModel;
    private ListQueryPublicModel listQueryPublicModel;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "get list dashboard save default and check")
    public void getListDashboardSaveDefault(){
        Map<String, Object> params = new HashMap<>();
        dashboardsListModel = (DashboardsListModel) dashboardMyWorkSteps.getListDashboardSave(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        queryPublicSteps.checkListDashboardAsc(dashboardsListModel);
    }

    @Test(description = "get list dashboard with date high to low and check")
    public void getListDashboardSaveDateHighToLow(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:desc");
        dashboardsListModel = (DashboardsListModel) dashboardMyWorkSteps.getListDashboardSave(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        queryPublicSteps.checkListDashboardDesc(dashboardsListModel);
    }

    @Test(description = "get list dashboard with date low to high and check")
    public void getListDashboardSaveDateLowToHigh(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:asc");
        dashboardsListModel = (DashboardsListModel) dashboardMyWorkSteps.getListDashboardSave(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DashboardsListModel.class);
        queryPublicSteps.checkListDashboardAsc(dashboardsListModel);
    }

    @Test(description = "get list query save default and check")
    public void getListQuerySaveDefault(){
        Map<String, Object> params = new HashMap<>();
        listQueryPublicModel = (ListQueryPublicModel) queryPublicSteps.getListQueriesSaved(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryPublicSteps.checkListQueryDesc(listQueryPublicModel);
    }

    @Test(description = "get list query save with date high to low and check")
    public void getListQuerySaveDateHighToLow(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:desc");
        listQueryPublicModel = (ListQueryPublicModel) queryPublicSteps.getListQueriesSaved(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryPublicSteps.checkListQueryDesc(listQueryPublicModel);
    }

    @Test(description = "get list query save with date low to high and check")
    public void getListQuerySaveDateLowToHigh(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:asc");
        listQueryPublicModel = (ListQueryPublicModel) queryPublicSteps.getListQueriesSaved(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryPublicSteps.checkListQueryAsc(listQueryPublicModel);
    }
}
