package my.test.query_public;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.querypublic.models.ExecuteQueryInput;
import microservices.querypublic.models.ExecuteQueryModel;
import microservices.querypublic.steps.QueryPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
@Feature("Query Public")
public class CheckEditStatementQueryTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private ExecuteQueryModel executeQueryModel = new ExecuteQueryModel();
    private String queryId = "KFOyZx44bSLKxdJzwh06c";
    private String statementNew = "select * from eth_mainnet.blocks limit 12";
    private String statementOld = "select * from eth_mainnet.blocks limit 10";
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "edit statement and run again")
    public void then_editStatementAndRunAgain(){
        ExecuteQueryInput executeQueryInput = new ExecuteQueryInput(queryId, statementNew);
        executeQueryModel = (ExecuteQueryModel) queryPublicSteps.executeAQuery(executeQueryInput).validateStatusCode(HttpURLConnection.HTTP_CREATED)
                .saveResponseObject(ExecuteQueryModel.class);
        queryPublicSteps.checkQueryAfterEditStatement(executeQueryModel, executeQueryInput);
    }
    @Test(description = "get execution", dependsOnMethods = {"then_editStatementAndRunAgain"})
    public void then_getExecution(){
        queryPublicSteps.getAnExecution(executeQueryModel.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @Test(description = "edit statement like begin", dependsOnMethods = {"then_getExecution"})
    public void then_editStatementLikeBegin(){
        ExecuteQueryInput executeQueryInput = new ExecuteQueryInput(queryId, statementOld);
        queryPublicSteps.executeAQuery(executeQueryInput).validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
}
