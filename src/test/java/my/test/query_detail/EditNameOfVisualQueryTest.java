package my.test.query_detail;

import core.BaseTest;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.querydetail.models.*;
import microservices.querydetail.steps.QueryDetailSteps;
import microservices.querylistmywork.models.ListBrowserQuery;
import microservices.querypublic.steps.QueryPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;

import static microservices.querydetail.constants.QueryDetailConstants.*;

public class EditNameOfVisualQueryTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private QueryDetailSteps queryDetailSteps = new QueryDetailSteps();
    private InsertVisualModel insertVisualModel;
    private ListBrowserQuery.QueryData dataQuery;
    private String queryId = "9vqx80R_lhy4NhnBK9Vuu";
    private String newName = "New Chart Name";
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }

    @Test(description = "check edit name of chart successfully", dataProvider = "dataChart")
    public void checkEditNameChart(String globalServiceType, String type, String name){
        //delete all visuals
        dataQuery = (ListBrowserQuery.QueryData)queryPublicSteps.findMyQuery(queryId).saveResponseObject(ListBrowserQuery.QueryData.class);
        queryDetailSteps.deleteAllOfVisualsOfQuery(dataQuery);
        //insert visual to a query
        OptionsInsertVisualModel options = new OptionsInsertVisualModel(globalServiceType);
        InsertVisualInput insertVisualInput = new InsertVisualInput(queryId, type, name, options);
        insertVisualModel = (InsertVisualModel) queryDetailSteps.insertVisual(insertVisualInput).validateStatusCode(HttpURLConnection.HTTP_CREATED)
                .saveResponseObject(InsertVisualModel.class);
        //check visual of query edited successfully
        EditVisualInput editVisualInput = new EditVisualInput(queryId, newName, options);
        queryDetailSteps.editAVisual(editVisualInput, insertVisualModel.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
        dataQuery = (ListBrowserQuery.QueryData)queryPublicSteps.findMyQuery(queryId).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListBrowserQuery.QueryData.class);
        queryDetailSteps.checkVisualEditedName(dataQuery, newName);
        //delete visual
        queryDetailSteps.deleteVisual(insertVisualModel.getId()).validateStatusCode(HttpURLConnection.HTTP_OK);
    }
    @DataProvider(name = "dataChart")
    public Object[][] dataChartInsert(){
        return new Object[][]{
                {GLOBAL_SERVICE_TYPE[0],TYPE_CHART[0], NAME_CHARTS[0]},
                {GLOBAL_SERVICE_TYPE[1],TYPE_CHART[0], NAME_CHARTS[1]},
                {GLOBAL_SERVICE_TYPE[2],TYPE_CHART[0], NAME_CHARTS[2]},
                {GLOBAL_SERVICE_TYPE[3],TYPE_CHART[0], NAME_CHARTS[3]},
                {GLOBAL_SERVICE_TYPE[4],TYPE_CHART[0], NAME_CHARTS[4]},
                {GLOBAL_SERVICE_TYPE[5],TYPE_CHART[1], NAME_CHARTS[5]},
                {GLOBAL_SERVICE_TYPE[0], TYPE_CHART[2], NAME_CHARTS[6]},
        };
    }
}
