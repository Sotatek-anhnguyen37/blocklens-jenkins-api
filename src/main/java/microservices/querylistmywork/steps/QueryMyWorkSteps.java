package microservices.querylistmywork.steps;

import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import microservices.dashboardmywork.steps.DashboardMyWorkSteps;
import microservices.dashboardpublic.steps.DashboardPublicSteps;
import microservices.querylistmywork.models.ListBrowserQuery;
import org.joda.time.DateTime;
import org.testng.Assert;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class QueryMyWorkSteps extends BaseApi {
    @Step("get query list in my work")
    public QueryMyWorkSteps getQueryListInMyWork(Map<String, Object> params){
        sendGet(Endpoint.Queries.LIST_BROWSE_QUERIES, params);
        return this;
    }
    @Step("verify query have name correct")
    public void verifyNameQueryAfterSearch(ListBrowserQuery list, String name){
        boolean temp = false;
        for(ListBrowserQuery.QueryData data : list.getData()){
            if (Objects.equals(data.getName(), name)) {
                temp = true;
                break;
            }
        }
        Assert.assertTrue(temp);
    }
    @Step("verify query not exist")
    public void verifyNameQueryAfterSearchNotExist(ListBrowserQuery list){
        Assert.assertEquals(list.getData().length, 0);
    }
    @Step("check query have multiple tags exist")
    public void checkQueryHaveMultipleTagsExist(ListBrowserQuery list){
        for(ListBrowserQuery.QueryData data: list.getData()){
            Assert.assertTrue(Arrays.toString(data.getTags()).contains("tag1") &&Arrays.toString(data.getTags()).contains("tag2"));
        }
    }
    @Step("check queries are sorted")
    public void checkQueriesSorted(ListBrowserQuery list, Map<String, Object> params){
        if(params.get("orderBy").toString().contains("asc")){
            for(int i = 1; i < list.getData().length; i++){
                DateTime date1 = DateTime.parse(list.getData()[i].getCreatedAt());
                DateTime date2 = DateTime.parse(list.getData()[i-1].getCreatedAt());
                Assert.assertTrue(date1.isAfter(date2));
            }
        }
        if(params.get("orderBy").toString().contains("desc")){
            for(int i = 1; i < list.getData().length; i++){
                DateTime date1 = DateTime.parse(list.getData()[i].getCreatedAt());
                DateTime date2 = DateTime.parse(list.getData()[i-1].getCreatedAt());
                Assert.assertTrue(date1.isBefore(date2));
            }
        }
    }
    @Step("Create a new query")
    public QueryMyWorkSteps createQuery(Object body){
        sendPost(Endpoint.Queries.CREATE_A_NEW_QUERY, body);
        validateResponse(HttpURLConnection.HTTP_CREATED);
        return this;
    }
    @Step("Delete a query")
    public QueryMyWorkSteps deleteQuery(String id){
        sendDelete(Endpoint.Queries.DELETE_QUERY, "queryId", id );
        return this;
    }
}
