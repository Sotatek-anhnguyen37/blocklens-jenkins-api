package microservices.querypublic.steps;

import constants.DateFormats;
import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.dashboardpublic.constants.DashboardsConstants;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.querylistmywork.models.ListBrowserQuery;
import microservices.querypublic.models.*;
import microservices.signin.model.SignInResponse;
import org.testng.Assert;
import util.CommonUtil;

import java.util.*;

public class QueryPublicSteps extends BaseApi {
    private SignInResponse signInResponse;
    @Step("sign in")
    public QueryPublicSteps when_signIn(Object body) {
        sendPost(Endpoint.BlocklensUsers.SIGN_IN_URL, body);
        signInResponse = (SignInResponse) saveResponseObject(SignInResponse.class);
        removeHeader("Authorization");
        setHeader("Authorization", "Bearer "+ signInResponse.getAccessToken());
        return this;
    }
    @Step("get tags treding")
    public QueryPublicSteps getTagsTrending(){
        sendGet(Endpoint.Queries.GET_TAGS_TRENDING);
        return this;
    }
    @Step("get list queries public")
    public QueryPublicSteps getListQueriesPublic(Map<String, Object> params){
        sendGet(Endpoint.Queries.GET_ALL_PUBLIC_QUERIES, params);
        return this;
    }
    @Step("check response of list queries public")
    public void checkResponseOfListQueryPublic(ListQueryPublicModel listQueriesModel){
        Assert.assertTrue(listQueriesModel.getItemCount() >=0 && listQueriesModel.getItemCount() <= listQueriesModel.getItemsPerPage());
        Assert.assertTrue(listQueriesModel.getCurrentPage() > 0 && listQueriesModel.getCurrentPage() <= listQueriesModel.getTotalPages());
        Assert.assertEquals(listQueriesModel.getData().length, listQueriesModel.getItemCount());
    }
    @Step("get filter query save")
    public QueryPublicSteps getFilterQuerySave(Map<String, Object> params){
        sendGet(Endpoint.QuerySaved.FILTER_QUERY_SAVE, params);
        return this;
    }
    @Step("create list query id")
    public List<String> createListQueryIds(ListQueryPublicModel listQueriesModel){
        List<String> queryIds = new ArrayList<>();
        for(DataQueryModel data : listQueriesModel.getData()){
            queryIds.add(data.getId());
        }
        return queryIds;
    }
    @Step("check list query is searched by name")
    public void checkListQuerySearchedByName(ListQueryPublicModel listQueriesModel, String name){
        for(DataQueryModel data : listQueriesModel.getData()){
            Assert.assertTrue(data.getName().toUpperCase().contains(name.toUpperCase()));
        }
    }
    @Step("check list query is got by tags")
    public void checkListQueryGotByTags(ListQueryPublicModel listQueriesModel, String[] tag){
        for(DataQueryModel data : listQueriesModel.getData()){
            Assert.assertTrue(Arrays.toString(data.getTags()).toUpperCase().contains(tag[0]));
        }
    }
    @Step("check list query sorted by created at")
    public void checkListQuerySortedByCreatedAt(ListQueryPublicModel queriesListModel, String orderBy){
        if(orderBy.toUpperCase().contains(DashboardsConstants.ORDER_BY[0])){
            for(int i = 1; i<queriesListModel.getData().length; i++){
                Assert.assertTrue(queriesListModel.getData()[i].get_createdAt() >= queriesListModel.getData()[i-1].get_createdAt());
            }
        }
        else{
            for(int i = 1; i<queriesListModel.getData().length; i++){
                Assert.assertTrue(queriesListModel.getData()[i].get_createdAt() <= queriesListModel.getData()[i-1].get_createdAt());
            }
        }
    }
    @Step("get detail query")
    public QueryPublicSteps getDetailQuery(String queryId){
        sendGet(Endpoint.Queries.GET_DETAIL_QUERY, "queryId", queryId);
        return this;
    }
    @Step("get an execution")
    public QueryPublicSteps getAnExecution(String executionId){
        Map<String, Object> params = new HashMap<>();
        params.put("executionId", executionId);
        sendGet(Endpoint.QueryExecutors.GET_AN_EXECUTION, params);
        return this;
    }
    @Step("check information detail query")
    public void checkInformationDetailQuery(DataQueryModel dataQueryModel){
        Assert.assertNotNull(dataQueryModel.getName());
        Assert.assertNotNull(dataQueryModel.getUserInfo().getFirstName());
        Assert.assertNotNull(dataQueryModel.getUserInfo().getLastName());
        System.out.println(dataQueryModel.getCreatedAt());
        Assert.assertTrue(CommonUtil.verifyFormatOfDate(dataQueryModel.getCreatedAt(), DateFormats.DATE_ISO_8601));
    }
    @Step("check information detail my query")
    public void checkInformationDetailMyQuery(ListBrowserQuery.QueryData data){
        Assert.assertEquals(data.getUserInfo().getEmail(), AccountScreenConstants.EMAIL);
        Assert.assertNotNull(data.getName());
        Assert.assertNotNull(data.getUserInfo().getLastName());
        Assert.assertNotNull(data.getUserInfo().getFirstName());
    }
    @Step("find my query by id")
    public QueryPublicSteps findMyQuery(String queryId){
        Map<String, Object> params = new HashMap<>();
        params.put("queryId", queryId);
        sendGet(Endpoint.Queries.FIND_MY_QUERY, params);
        return this;
    }
    @Step("get many schemas")
    public QueryPublicSteps getManySchemas(){
        sendGet(Endpoint.Queries.GET_MANY_SCHEMAS);
        return this;
    }
    @Step("get list browser queries")
    public QueryPublicSteps getListBrowserQueries(Map<String, Object> params){
        sendGet(Endpoint.Queries.LIST_BROWSE_QUERIES, params);
        return this;
    }
    @Step("get query id of my query")
    public List<String> getMyQueryId(ListQueryPublicModel listQuery){
        List<String> listQueryId = new ArrayList<>();
        for(DataQueryModel data: listQuery.getData()){
            if(Objects.equals(data.getUserInfo().getEmail(), AccountScreenConstants.EMAIL)){
                listQueryId.add(data.getId());
            }
        }
        return listQueryId;
    }
    @Step("save query")
    public QueryPublicSteps saveQuery(String queryId){
        sendPost(Endpoint.QuerySaved.QUERY_SAVE, new SaveQueryInput(queryId));
        return this;
    }
    @Step("delete query is saved")
    public QueryPublicSteps deleteQuerySave(String queryId){
        sendDelete(Endpoint.QuerySaved.QUERY_SAVE, new SaveQueryInput(queryId));
        return this;
    }
    @Step("get list queries saved")
    public QueryPublicSteps getListQueriesSaved(Map<String, Object> params){
        sendGet(Endpoint.QuerySaved.QUERY_SAVE, params);
        return this;
    }
    @Step("check query have saved")
    public void checkQueryHaveSaved(ListQueryPublicModel listQueryPublicModel, String queryId){
        boolean temp = false;
        for(DataQueryModel data : listQueryPublicModel.getData()){
            if (Objects.equals(data.getId(), queryId)) {
                temp = true;
                break;
            }
        }
        Assert.assertTrue(temp);
    }
    @Step("fork query")
    public QueryPublicSteps forKQuery(ForkQueryInput forkQueryInput, String queryId){
        sendPost(Endpoint.Queries.FORK_A_QUERY, forkQueryInput, "queryId", queryId);
        return this;
    }
    @Step("check information query forked")
    public void checkInformationQueryForked(ForkQueryModel forkQueryModel, QueryModel queryModel){
        Assert.assertEquals(forkQueryModel.getForkedQueryId(), queryModel.getId());
        Assert.assertEquals(forkQueryModel.getQuery(), queryModel.getQuery());
        Assert.assertEquals(forkQueryModel.getExecutedId(), queryModel.getExecutedId());
        Assert.assertEquals(forkQueryModel.getUtilizedChains(), queryModel.getUtilizedChains());
        Assert.assertEquals(forkQueryModel.getUtilizedChains(), queryModel.getUtilizedChains());
        Assert.assertEquals(forkQueryModel.getVisualizations().length, queryModel.getVisualizations().length);
    }
    @Step("delete query")
    public QueryPublicSteps deleteQuery(String queryId){
        sendDelete(Endpoint.Queries.DELETE_QUERY, "queryId", queryId);
        return this;
    }
    @Step("execute a query")
    public QueryPublicSteps executeAQuery(ExecuteQueryInput executeQueryInput){
        sendPost(Endpoint.QueryExecutors.EXECUTE_A_QUERY, executeQueryInput);
        return this;
    }
    @Step("cancel execute query")
    public QueryPublicSteps cancelExecuteQuery(String executionId){
        sendPost(Endpoint.QueryExecutors.CANCEL_A_QUERY_EXECUTION, "executionId", executionId);
        return this;
    }
    @Step("check query after edit statment")
    public void checkQueryAfterEditStatement(ExecuteQueryModel executeQueryModel, ExecuteQueryInput executeQueryInput){
        Assert.assertEquals(executeQueryModel.getQueryStatement(), executeQueryInput.getStatement());
        Assert.assertEquals(executeQueryModel.getQueryId(), executeQueryInput.getId());
    }

    @Step("check list dashboard save order by desc")
    public void checkListDashboardDesc(DashboardsListModel dashboardsListModel){
        if(dashboardsListModel.getData().length >=2){
            for(int i =1; i<dashboardsListModel.getData().length; i++){
                Assert.assertTrue(dashboardsListModel.getData()[i].get_createdAt() <= dashboardsListModel.getData()[i-1].get_createdAt());
            }
        }
    }
    @Step("check list dashboard save order by asc")
    public void checkListDashboardAsc(DashboardsListModel dashboardsListModel){
        if(dashboardsListModel.getData().length >=2){
            for(int i =1; i<dashboardsListModel.getData().length; i++){
                Assert.assertTrue(dashboardsListModel.getData()[i].get_createdAt() >= dashboardsListModel.getData()[i-1].get_createdAt());
            }
        }
    }
    @Step("check list query save order by desc")
    public void checkListQueryDesc(ListQueryPublicModel listQueryPublicModel){
        if(listQueryPublicModel.getData().length >=2){
            for(int i = 1; i < listQueryPublicModel.getData().length; i++){
                Assert.assertTrue(listQueryPublicModel.getData()[i].get_createdAt() <= listQueryPublicModel.getData()[i-1].get_createdAt());
            }
        }
    }
    @Step("check list query save order by asc")
    public void checkListQueryAsc(ListQueryPublicModel listQueryPublicModel){
        if(listQueryPublicModel.getData().length >=2){
            for(int i =1; i<listQueryPublicModel.getData().length; i++){
                Assert.assertTrue(listQueryPublicModel.getData()[i].get_createdAt() >= listQueryPublicModel.getData()[i-1].get_createdAt());
            }
        }
    }
    @Step("verify query not exist")
    public void verifyNameQueryAfterSearchNotExist(ListQueryPublicModel list){
        Assert.assertEquals(list.getData().length, 0);
    }
}
