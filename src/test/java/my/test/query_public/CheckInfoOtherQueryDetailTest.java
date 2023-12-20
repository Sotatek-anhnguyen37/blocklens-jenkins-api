package my.test.query_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.querypublic.models.DataQueryModel;
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
public class CheckInfoOtherQueryDetailTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private ListQueryPublicModel listQueries = new ListQueryPublicModel();
    private DataQueryModel dataQueryModel = new DataQueryModel();
    private String queryId;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "get list query")
    public void getListQuery(){
        Map<String, Object> params = new HashMap<>();
        listQueries = (ListQueryPublicModel) queryPublicSteps.getListQueriesPublic(params)
                .validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryId = listQueries.getData()[0].getId();
    }
    @Test(description = "get filter queries save", dependsOnMethods = {"getListQuery"})
    public void then_filterQueriesSave(){
        List<String> queryIds = queryPublicSteps.createListQueryIds(listQueries);
        Map<String, Object> params = new HashMap<>();
        params.put("queryIds", queryIds);
        queryPublicSteps.getFilterQuerySave(params).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "get information detail query", dependsOnMethods = {"then_filterQueriesSave"})
    public void then_getInfoDetailQuery(){
        dataQueryModel = (DataQueryModel) queryPublicSteps.getDetailQuery(queryId).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(DataQueryModel.class);
    }
    @Test(description = "get filter queries save", dependsOnMethods = {"then_getInfoDetailQuery"})
    public void then_filterQueriesSave2(){
        List<String> queryIds = new ArrayList<>();
        queryIds.add(queryId);
        Map<String, Object> params = new HashMap<>();
        params.put("queryIds", queryIds);
        queryPublicSteps.getFilterQuerySave(params).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "get an execution", dependsOnMethods = {"then_getInfoDetailQuery"})
    public void then_getAnExecution(){
        queryPublicSteps.getAnExecution(dataQueryModel.getExecutedId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "check detail information of query", dependsOnMethods = {"then_getAnExecution"})
    public void then_checkInformationDetailQuery(){
        queryPublicSteps.checkInformationDetailQuery(dataQueryModel);
    }
}
