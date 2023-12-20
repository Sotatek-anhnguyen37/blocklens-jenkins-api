package my.test.query_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.querypublic.models.ForkQueryInput;
import microservices.querypublic.models.QueryModel;
import microservices.querypublic.steps.QueryPublicSteps;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;

@Feature("Query Public")
public class CheckForkUserHaveNotLoginYetTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private QueryModel queryModel = new QueryModel();
    private String queryId = "ajkobWHpxXVNAYjQT6YLq";
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
        queryPublicSteps.forKQuery(new ForkQueryInput("newQueryName"), queryId).validateStatusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
    }
}
