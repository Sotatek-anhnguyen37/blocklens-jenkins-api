package my.test.searchTags;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardmywork.steps.DashboardMyWorkSteps;
import microservices.dashboardpublic.constants.DashboardsConstants;
import microservices.dashboardpublic.models.CreateDashboardInput;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import microservices.querylistmywork.models.CreateQueryInput;
import microservices.querylistmywork.steps.QueryMyWorkSteps;
import microservices.searchtags.model.DashboardResponse;
import microservices.searchtags.model.QueryResponse;
import microservices.searchtags.model.SearchTagsResponse;
import microservices.searchtags.steps.SearchTagsSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static microservices.querylistmywork.constants.StatementQueryConstants.QUERY_STATEMENT;
import static util.CommonUtil.getRandomString;

@Feature("Search tags")
public class SearchTags extends BaseTest {
    private DashboardResponse.DataDashboard dashboard;
    private QueryResponse.QueryData query;
    private DashboardMyWorkSteps dashboardMyWorkSteps = new DashboardMyWorkSteps();
    private DashboardPublicSteps dashboardPublicStepsStep = new DashboardPublicSteps();
    private QueryMyWorkSteps queryMyWorkSteps = new QueryMyWorkSteps();

    SearchTagsSteps searchTagsSteps = new SearchTagsSteps();
    @BeforeMethod(alwaysRun = true, onlyForGroups = {"login"})
    public void signIn(){
        searchTagsSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL_NGOC, AccountScreenConstants.PASS_WORD_NGOC));
    }
    @Test(description = "User not login - Search tag in Dashboards", groups = {"regression", "searchTags", "blocklens-query-executor"})
    public void searchHashtagMatchingAPart() {
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_getTags("te", 1, 20)
                        .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "te");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("test1");
        listString.add("test1");
        DashboardResponse response = (DashboardResponse) searchTagsSteps.when_getAllPublicTags(listString)
                .saveResponseObject(DashboardResponse.class);
        DashboardResponse.DataDashboard[] data = response.getData();
        searchTagsSteps.checkHashTag("test1", data);
    }
    @Test(description = "User not login - Search tag in Dashboards", groups = {"regression", "searchTags", "blocklens-query-executor"})
    public void searchHashtagMatchingFully() {
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_getTags("test1", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "test1");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("test1");
        listString.add("test1");
        DashboardResponse response = (DashboardResponse) searchTagsSteps.when_getAllPublicTags(listString)
                .saveResponseObject(DashboardResponse.class);
        DashboardResponse.DataDashboard[] data = response.getData();
        searchTagsSteps.checkHashTag("test1", data);
    }
    @Test(description = "User not login - Search tag in Query", groups = {"regression", "searchTags", "blocklens-query-executor"})
    public void searchHashtagMatchingAPartInQuery() {
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_searchQueryTags("te", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "te");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("test1");
        listString.add("test1");
        QueryResponse response = (QueryResponse) searchTagsSteps.when_getAllPublicQueryTags(listString)
                .saveResponseObject(QueryResponse.class);
        QueryResponse.QueryData[] data = response.getData();
        searchTagsSteps.checkHashTagsInQuery("test1", data);
    }
    @Test(description = "User not login - Search tag in Query", groups = {"regression", "searchTags", "blocklens-query-executor"})
    public void searchHashtagMatchingFullyInQuery() {
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_searchQueryTags("test1", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "test1");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("test1");
        listString.add("test1");
        QueryResponse response = (QueryResponse) searchTagsSteps.when_getAllPublicQueryTags(listString)
                .saveResponseObject(QueryResponse.class);
        QueryResponse.QueryData[] data = response.getData();
        searchTagsSteps.checkHashTagsInQuery("test1", data);
    }
    @Test(description = "User login - Search tag in My Work - Dashboard", groups = {"regression", "searchTags", "blocklens-query-executor", "login"})
    public void searchHashtagMatchingAPartInMyWorkDash() {
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_searchMyWorkDashTags("te", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "te");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("test1");
        listString.add("test1");
        DashboardResponse response = (DashboardResponse) searchTagsSteps.when_getListBrowseDashboards(listString)
                .saveResponseObject(DashboardResponse.class);
        DashboardResponse.DataDashboard[] data = response.getData();
        searchTagsSteps.checkHashTag("test1", data);
    }
    @Test(description = "User login - Search tag in My Work - Dashboard", groups = {"regression", "searchTags", "blocklens-query-executor", "login"})
    public void searchHashtagMatchingFullyInMyWorkDash() {
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_searchMyWorkDashTags("ngoc", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "ngoc");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("ngoc");
        listString.add("ngoc");
        DashboardResponse response = (DashboardResponse) searchTagsSteps.when_getListBrowseDashboards(listString)
                .saveResponseObject(DashboardResponse.class);
        DashboardResponse.DataDashboard[] data = response.getData();
        searchTagsSteps.checkHashTag("ngoc", data);
    }
    @Test(description = "User login - Search tag in My Work - Dashboard", groups = {"regression", "searchTags", "blocklens-query-executor", "login"})
    public void searchHashtagMatchingAPartInMyWorkQuery() {
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_searchMyWorkQueryTags("pu", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "pu");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("pupu");
        listString.add("pupu");
        QueryResponse response = (QueryResponse) searchTagsSteps.when_getListBrowseQuery(listString)
                .saveResponseObject(QueryResponse.class);
        QueryResponse.QueryData[] data = response.getData();
        searchTagsSteps.checkHashTagsInQuery("pupu", data);
    }
    @Test(description = "User login - Search tag in My Work - Dashboard", groups = {"regression", "searchTags", "blocklens-query-executor", "login"})
    public void searchHashtagMatchingFullyInMyWorkQuery() {
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_searchMyWorkQueryTags("noo", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "noo");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("noo");
        listString.add("noo");
        QueryResponse response = (QueryResponse) searchTagsSteps.when_getListBrowseQuery(listString)
                .saveResponseObject(QueryResponse.class);
        QueryResponse.QueryData[] data = response.getData();
        searchTagsSteps.checkHashTagsInQuery("noo", data);
    }
    @Test(description = "Search for a hashtag does not exist", groups = {"regression", "searchTags", "blocklens-query-executor", "login"})
    public void searchAndGetForHashtagDosesNotExistInPublicDash() {
        String[] tags = {"doNotExist"};
        CreateDashboardInput createDashboardInput = new CreateDashboardInput(DashboardsConstants.DASHBOARD_NAME_WIDGET, true, tags);
        dashboard = (DashboardResponse.DataDashboard) dashboardPublicStepsStep.createDashboard(createDashboardInput)
                .saveResponseObject(DashboardResponse.DataDashboard.class);
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_getTags("doNotExist", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "doNotExist");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("doNotExist");
        listString.add("doNotExist");
        DashboardResponse response = (DashboardResponse) searchTagsSteps.when_getAllPublicTags(listString)
                .saveResponseObject(DashboardResponse.class);
        DashboardResponse.DataDashboard[] data = response.getData();
        searchTagsSteps.checkHashTag("doNotExist", data);
        dashboardMyWorkSteps.deleteDashboard(dashboard.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
        SearchTagsResponse searchTagsResponse1 = (SearchTagsResponse) searchTagsSteps.when_getTags("doNotExist", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkLResponseWhenDataNull(searchTagsResponse1);
    }
    @Test(description = "Search for a hashtag does not exist", groups = {"regression", "searchTags", "blocklens-query-executor", "login"})
    public void searchAndGetForHashtagDosesNotExistInPublicQuery() {
        String[] tags = {"doNotExistQuery"};
        CreateQueryInput createQueryInput = new CreateQueryInput(getRandomString(21), DashboardsConstants.DASHBOARD_NAME_WIDGET, QUERY_STATEMENT, tags);
        query = (QueryResponse.QueryData) queryMyWorkSteps.createQuery(createQueryInput)
                .saveResponseObject(QueryResponse.QueryData.class);
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_searchQueryTags("doNotExistQuery", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "doNotExistQuery");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("doNotExistQuery");
        listString.add("doNotExistQuery");
        QueryResponse response = (QueryResponse) searchTagsSteps.when_getAllPublicQueryTags(listString)
                .saveResponseObject(QueryResponse.class);
        QueryResponse.QueryData[] data = response.getData();
        searchTagsSteps.checkHashTagsInQuery("doNotExistQuery", data);
        queryMyWorkSteps.deleteQuery(query.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
        SearchTagsResponse searchTagsResponse1 = (SearchTagsResponse) searchTagsSteps.when_searchQueryTags("doNotExistQuery", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkLResponseWhenDataNull(searchTagsResponse1);
        SearchTagsResponse response1 = (SearchTagsResponse) searchTagsSteps.when_getAllPublicQueryTags(listString)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkLResponseWhenDataNull(response1);
    }
    @Test(description = "Search for a hashtag does not exist", groups = {"regression", "searchTags", "blocklens-query-executor", "login"})
    public void searchAndGetForHashtagDosesNotExistInMyWorkQuery() {
        String[] tags = {"myWorkQuery"};
        CreateQueryInput createQueryInput = new CreateQueryInput(getRandomString(21), DashboardsConstants.DASHBOARD_NAME_WIDGET, QUERY_STATEMENT, tags);
        query = (QueryResponse.QueryData) queryMyWorkSteps.createQuery(createQueryInput)
                .saveResponseObject(QueryResponse.QueryData.class);
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_searchMyWorkQueryTags("myWorkQuery", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "myWorkQuery");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("myWorkQuery");
        listString.add("myWorkQuery");
        QueryResponse response = (QueryResponse) searchTagsSteps.when_getListBrowseQuery(listString)
                .saveResponseObject(QueryResponse.class);
        QueryResponse.QueryData[] data = response.getData();
        searchTagsSteps.checkHashTagsInQuery("myWorkQuery", data);
        queryMyWorkSteps.deleteQuery(query.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
        SearchTagsResponse searchTagsResponse1 = (SearchTagsResponse) searchTagsSteps.when_searchMyWorkQueryTags("myWorkQuery", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkLResponseWhenDataNull(searchTagsResponse1);
        SearchTagsResponse response1 = (SearchTagsResponse) searchTagsSteps.when_getListBrowseQuery(listString)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkLResponseWhenDataNull(response1);
    }
    @Test(description = "Search for a hashtag does not exist", groups = {"regression", "searchTags", "blocklens-query-executor", "login"})
    public void searchAndGetForHashtagDosesNotExistInMyWorkDash() {
        String[] tags = {"notExist"};
        CreateDashboardInput createDashboardInput = new CreateDashboardInput(DashboardsConstants.DASHBOARD_NAME_WIDGET, true, tags);
        dashboard = (DashboardResponse.DataDashboard) dashboardPublicStepsStep.createDashboard(createDashboardInput)
                .saveResponseObject(DashboardResponse.DataDashboard.class);
        SearchTagsResponse searchTagsResponse = (SearchTagsResponse) searchTagsSteps.when_searchMyWorkDashTags("notExist", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkListOfTags(searchTagsResponse, "notExist");
        ArrayList<String> listString = new ArrayList<String>();
        listString.add("notExist");
        listString.add("notExist");
        DashboardResponse response = (DashboardResponse) searchTagsSteps.when_getListBrowseDashboards(listString)
                .saveResponseObject(DashboardResponse.class);
        DashboardResponse.DataDashboard[] data = response.getData();
        searchTagsSteps.checkHashTag("notExist", data);
        dashboardMyWorkSteps.deleteDashboard(dashboard.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
        SearchTagsResponse searchTagsResponse1 = (SearchTagsResponse) searchTagsSteps.when_searchMyWorkDashTags("doNotExist", 1, 20)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkLResponseWhenDataNull(searchTagsResponse1);
        SearchTagsResponse response1 = (SearchTagsResponse) searchTagsSteps.when_getListBrowseDashboards(listString)
                .saveResponseObject(SearchTagsResponse.class);
        searchTagsSteps.checkLResponseWhenDataNull(response1);
    }
}
