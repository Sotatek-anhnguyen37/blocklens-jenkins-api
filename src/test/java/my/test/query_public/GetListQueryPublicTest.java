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
public class GetListQueryPublicTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private ListQueryPublicModel listQueriesModel;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "get list tags trending")
    public void getTagTrending(){
        queryPublicSteps.getTagsTrending().validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "get list queries public successfully")
    public void then_getListQueriesPublicSuccessfully(){
        Map<String, Object> params = new HashMap<>();
        listQueriesModel = (ListQueryPublicModel) queryPublicSteps.getListQueriesPublic(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListQueryPublicModel.class);
        queryPublicSteps.checkResponseOfListQueryPublic(listQueriesModel);
    }
    @Test(description = "get filter queries save", dependsOnMethods = {"then_getListQueriesPublicSuccessfully"})
    public void then_filterQueriesSave(){
        List<String> queryIds = queryPublicSteps.createListQueryIds(listQueriesModel);
        Map<String, Object> params = new HashMap<>();
        params.put("queryIds", queryIds);
        queryPublicSteps.getFilterQuerySave(params).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
}
