package my.test.query_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.querypublic.constants.QueryPublicConstants;
import microservices.querypublic.models.ListQueryPublicModel;
import microservices.querypublic.steps.QueryPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Feature("Query Public")
public class GetListQueryByNameTest extends BaseTest {
    private QueryPublicSteps querySteps = new QueryPublicSteps();
    private ListQueryPublicModel listQueriesModel;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        querySteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "get list query public by name")
    public void getListQueryByName(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", QueryPublicConstants.NAME_QUERY);
        listQueriesModel = (ListQueryPublicModel) querySteps.getListQueriesPublic(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
    }
    @Test(description = "get filter queries save", dependsOnMethods = {"getListQueryByName"})
    public void then_filterQueriesSave(){
        List<String> queryIds = querySteps.createListQueryIds(listQueriesModel);
        Map<String, Object> params = new HashMap<>();
        params.put("queryIds", queryIds);
        querySteps.getFilterQuerySave(params).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "check list query", dependsOnMethods = {"getListQueryByName"})
    public void then_checkListQuery(){
        querySteps.checkListQuerySearchedByName(listQueriesModel, QueryPublicConstants.NAME_QUERY);
    }
}
