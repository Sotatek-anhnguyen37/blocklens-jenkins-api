package my.test.query_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.querylistmywork.models.ListBrowserQuery;
import microservices.querypublic.models.ListQueryPublicModel;
import microservices.querypublic.steps.QueryPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Feature("Query Public")
public class CheckInfoMyQueryDetailTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private ListQueryPublicModel listQueries = new ListQueryPublicModel();
    private ListBrowserQuery.QueryData dataQueryModel;
    private String queryId;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "get list query")
    public void getListQuery(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", "ducanh");
        listQueries = (ListQueryPublicModel) queryPublicSteps.getListQueriesPublic(params)
                .validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryId = queryPublicSteps.getMyQueryId(listQueries).get(0);
    }
    @Test(description = "find my query by queryId", dependsOnMethods = {"getListQuery"})
    public void then_findMyQuery(){
        dataQueryModel = (ListBrowserQuery.QueryData) queryPublicSteps.findMyQuery(queryId).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListBrowserQuery.QueryData.class);
    }
    @Test(description = "get list browser queries")
    public void then_getListBrowserQueries(){
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 40);
        queryPublicSteps.getListBrowserQueries(params).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "get many schemas")
    public void then_getManySchemas(){
        queryPublicSteps.getManySchemas().validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "filter query")
    public void then_filterQuery(){
        List<String> queryIds = new ArrayList<>();
        queryIds.add(queryId);
        Map<String, Object> params = new HashMap<>();
        params.put("queryIds", queryIds);
        queryPublicSteps.getFilterQuerySave(params).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "get an execution", dependsOnMethods = {"then_filterQuery"})
    public void then_getAnExecution(){
        queryPublicSteps.getAnExecution(dataQueryModel.getExecutedId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "check information detail my query", dependsOnMethods = {"then_getAnExecution"})
    public void then_checkInfoDetailMyQuery(){
        queryPublicSteps.checkInformationDetailMyQuery(dataQueryModel);
    }
}
