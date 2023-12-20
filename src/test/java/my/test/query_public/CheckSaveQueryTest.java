package my.test.query_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.querypublic.models.ListQueryPublicModel;
import microservices.querypublic.steps.QueryPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.*;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Feature("Query Public")
public class CheckSaveQueryTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private ListQueryPublicModel listQueries = new ListQueryPublicModel();
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
        listQueries = (ListQueryPublicModel) queryPublicSteps.getListQueriesPublic(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryId = queryPublicSteps.getMyQueryId(listQueries).get(0);
    }
    @Test(description = "save query", dependsOnMethods = {"getListQuery"})
    public void then_saveQuery(){
        queryPublicSteps.saveQuery(queryId).validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "get list queries saved", dependsOnMethods = {"then_saveQuery"})
    public void then_getListQueriesSaved(){
        Map<String, Object> params = new HashMap<>();
        listQueries = (ListQueryPublicModel) queryPublicSteps.getListQueriesSaved(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryPublicSteps.checkQueryHaveSaved(listQueries, queryId);
    }
    @Test(description = "delete query saved", dependsOnMethods = {"then_getListQueriesSaved"})
    public void then_deleteQuerySaved(){
        queryPublicSteps.deleteQuerySave(queryId).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
}
