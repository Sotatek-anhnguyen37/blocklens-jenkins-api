package my.test.query_detail;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.querydetail.models.InsertVisualInput;
import microservices.querydetail.models.InsertVisualModel;
import microservices.querydetail.models.OptionsInsertVisualModel;
import microservices.querydetail.steps.QueryDetailSteps;
import microservices.querylistmywork.models.ListBrowserQuery;
import microservices.querypublic.steps.QueryPublicSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static microservices.querydetail.constants.QueryDetailConstants.*;

@Feature("Query Detail")
public class InsertVisualToQueryTest extends BaseTest {
    private QueryPublicSteps queryPublicSteps = new QueryPublicSteps();
    private QueryDetailSteps queryDetailSteps = new QueryDetailSteps();
    private InsertVisualModel insertVisualModel = new InsertVisualModel();
    ListBrowserQuery.QueryData queryData;
    private String queryId = "9vqx80R_lhy4NhnBK9Vuu";

    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        queryPublicSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD))
                .validateStatusCode(HttpURLConnection.HTTP_CREATED);
    }
    @Test(description = "delete all of visuals of query")
    public void deleteAllOfVisualsOfQuery(){
        queryData = (ListBrowserQuery.QueryData)queryPublicSteps.findMyQuery(queryId).saveResponseObject(ListBrowserQuery.QueryData.class);
        queryDetailSteps.deleteAllOfVisualsOfQuery(queryData);
    }
    @Test(description = "insert a new visual to a query successfully", dataProvider = "dataChart", dependsOnMethods = {"deleteAllOfVisualsOfQuery"})
    public void insertNewVisualIntoQuerySuccess(String globalServicesType, String type, String name){
        OptionsInsertVisualModel options = new OptionsInsertVisualModel(globalServicesType);
        InsertVisualInput insertVisualInput = new InsertVisualInput(queryId, type, name, options);
        insertVisualModel = (InsertVisualModel) queryDetailSteps.insertVisual(insertVisualInput).validateStatusCode(HttpURLConnection.HTTP_CREATED)
                .saveResponseObject(InsertVisualModel.class);
        //check query inserted visual successfully
        queryData = (ListBrowserQuery.QueryData) queryPublicSteps.findMyQuery(queryId).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListBrowserQuery.QueryData.class);
        queryDetailSteps.checkQueryInsertedVisual(queryData, insertVisualModel, name);
        //filter query
        List<String> queryIds = new ArrayList<>();
        queryIds.add(queryId);
        Map<String, Object> params = new HashMap<>();
        params.put("queryIds", queryIds);
        queryPublicSteps.getFilterQuerySave(params).validateStatusCode(HttpURLConnection.HTTP_OK);
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
    @Test(description = "insert a new visual to a query unsuccessfully", dataProvider = "dataChartInvalid", dependsOnMethods = {"deleteAllOfVisualsOfQuery"})
    public void insertNewVisualIntoQueryFail(String queryId, String type, String name){
        OptionsInsertVisualModel options = new OptionsInsertVisualModel();
        InsertVisualInput insertVisualInput = new InsertVisualInput(queryId, type, name, options);
        queryDetailSteps.insertVisual(insertVisualInput).validateStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @DataProvider(name = "dataChartInvalid")
    public Object[][] dataChartInsertInvalid(){
        return new Object[][]{
                {"", "", ""},
                {"", TYPE_CHART[1], NAME_CHARTS[6]},
                {queryId, "", NAME_CHARTS[6]},
                {queryId, TYPE_CHART[1], ""},

                {"  ", "  ", "  "},
                {"  ", TYPE_CHART[1], NAME_CHARTS[6]},
                {queryId, "  ", NAME_CHARTS[6]},
        };
    }
}
