package my.test.query_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.querypublic.models.ForkQueryInput;
import microservices.querypublic.models.ForkQueryModel;
import microservices.querypublic.models.QueryModel;
import microservices.querypublic.steps.QueryPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;

@Feature("Query Public")
public class CheckForkUserLoggedInTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private QueryModel queryModel = new QueryModel();
    private ForkQueryModel forkQueryModel;
    private String queryId = "KyTYKY26ccCuqyqTnFpqc";
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "get detail query")
    public void getDetailQuery(){
        queryModel = (QueryModel) queryPublicSteps.getDetailQuery(queryId).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(QueryModel.class);
    }
    @Test(description = "get execution", dependsOnMethods = {"getDetailQuery"})
    public void then_getExecution(){
        queryPublicSteps.getAnExecution(queryModel.getExecutedId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "fork query", dependsOnMethods = {"then_getExecution"})
    public void then_forkQuery(){
        forkQueryModel = (ForkQueryModel) queryPublicSteps.forKQuery(new ForkQueryInput("newQueryName"), queryId).validateStatusCode(HttpURLConnection.HTTP_CREATED)
                .saveResponseObject(ForkQueryModel.class);
    }
    @Test(description = "check information of query forked", dependsOnMethods = {"then_forkQuery"})
    public void then_checkInformationQueryForked(){
        queryPublicSteps.checkInformationQueryForked(forkQueryModel, queryModel);
    }
    @Test(description = "delete query forked", dependsOnMethods = {"then_forkQuery"})
    public void then_deleteQueryForked(){
        queryPublicSteps.deleteQuery(forkQueryModel.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
}
