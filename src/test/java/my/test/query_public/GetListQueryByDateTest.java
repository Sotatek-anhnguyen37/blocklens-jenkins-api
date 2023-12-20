package my.test.query_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
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
public class GetListQueryByDateTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private ListQueryPublicModel queriesListModel;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "check list query when click choose Date low to high")
    public void checkListQueryWhenClickChooseDateLowToHigh(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:asc");
        queriesListModel = (ListQueryPublicModel) queryPublicSteps.getListQueriesPublic(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryPublicSteps.checkListQuerySortedByCreatedAt(queriesListModel, params.get("orderBy").toString());
    }
    @Test(description = "get filter queries save", dependsOnMethods = {"checkListQueryWhenClickChooseDateLowToHigh"})
    public void then_filterQueriesSave(){
        List<String> queryIds = queryPublicSteps.createListQueryIds(queriesListModel);
        Map<String, Object> params = new HashMap<>();
        params.put("queryIds", queryIds);
        queryPublicSteps.getFilterQuerySave(params).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "check list query when click choose Date high to low")
    public void checkListQueryWhenClickChooseDateHighToLow(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:desc");
        queriesListModel = (ListQueryPublicModel) queryPublicSteps.getListQueriesPublic(params)
                .validateResponse(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryPublicSteps.checkListQuerySortedByCreatedAt(queriesListModel, params.get("orderBy").toString());
    }
    @Test(description = "get filter queries save", dependsOnMethods = {"checkListQueryWhenClickChooseDateHighToLow"})
    public void then_filterQueriesSave2(){
        List<String> queryIds = queryPublicSteps.createListQueryIds(queriesListModel);
        Map<String, Object> params = new HashMap<>();
        params.put("queryIds", queryIds);
        queryPublicSteps.getFilterQuerySave(params).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
}
