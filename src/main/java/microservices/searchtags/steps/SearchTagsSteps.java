package microservices.searchtags.steps;

import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import microservices.searchtags.model.DashboardResponse;
import microservices.searchtags.model.QueryResponse;
import microservices.searchtags.model.SearchTagsResponse;
import microservices.signin.model.SignInResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchTagsSteps extends BaseApi {
    private SignInResponse signInResponse;

    @Step("Search dashboard tags")
    public SearchTagsSteps when_getTags(String search, int page, int limit) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("page", page);
        param.put("limit", limit);
        sendGet(Endpoint.Dashboards.PUBLIC_GET_TAGS, param);
        return this;
    }

    @Step("Get all public dashboards when search by tags")
    public SearchTagsSteps when_getAllPublicTags(ArrayList<String> tags) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tags", tags);
        sendGet(Endpoint.Dashboards.GET_ALL_PUBLIC_DASHBOARDS, param);
        return this;
    }

    @Step("Check the list of tags that contain the search text")
    public void checkListOfTags(SearchTagsResponse searchTagsResponse, String search) {
        validateResponse(HttpStatus.SC_OK);
        for (String data : searchTagsResponse.getData()) {
            Assert.assertTrue(data.toLowerCase().contains(search.toLowerCase()));
        }

    }
    @Step("Check response of list dashboards that contain tags when searching by tags")
    public void checkHashTag(String tag, DashboardResponse.DataDashboard[] data) {
        validateResponse(HttpStatus.SC_OK);
        for (DashboardResponse.DataDashboard item : data) {
            Assert.assertTrue(item.getTags().contains(tag));
        }
    }

    @Step("Search query tags")
    public SearchTagsSteps when_searchQueryTags(String search, int page, int limit) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("page", page);
        param.put("limit", limit);
        sendGet(Endpoint.Queries.PUBLIC_SEARCH_QUERY_TAGS, param);
        return this;
    }

    @Step("sign in")
    public SearchTagsSteps when_signIn(Object body) {
        sendPost(Endpoint.BlocklensUsers.SIGN_IN_URL, body);
        signInResponse = (SignInResponse) saveResponseObject(SignInResponse.class);
        removeHeader("Authorization");
        setHeader("Authorization", "Bearer " + signInResponse.getAccessToken());
        return this;
    }

    @Step("Get all public query tags")
    public SearchTagsSteps when_getAllPublicQueryTags(ArrayList<String> tags) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tags", tags);
        sendGet(Endpoint.Queries.GET_ALL_PUBLIC_QUERY_TAGS, param);
        return this;
    }

    @Step("Check response of list query that contain tags when searching by tags")
    public void checkHashTagsInQuery(String tag, QueryResponse.QueryData[] data) {
        validateResponse(HttpStatus.SC_OK);
        for (QueryResponse.QueryData item : data) {
            Assert.assertTrue(item.getTags().contains(tag));
        }
    }

    @Step("Search my work dash tags")
    public SearchTagsSteps when_searchMyWorkDashTags(String search, int page, int limit) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("page", page);
        param.put("limit", limit);
        sendGet(Endpoint.Dashboards.SEARCH_MY_DASHBOARD_TAGS, param);
        return this;
    }

    @Step("Search my work query tags")
    public SearchTagsSteps when_searchMyWorkQueryTags(String search, int page, int limit) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("search", search);
        param.put("page", page);
        param.put("limit", limit);
        sendGet(Endpoint.Queries.SEARCH_MY_QUERY_TAGS, param);
        return this;
    }

    @Step("Get all public dashboards tags")
    public SearchTagsSteps when_getListBrowseDashboards(ArrayList<String> tags) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tags", tags);
        sendGet(Endpoint.Dashboards.LIST_BROWSE_DASHBOARDS, param);
        return this;
    }

    @Step("Get all public query tags")
    public SearchTagsSteps when_getListBrowseQuery(ArrayList<String> tags) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tags", tags);
        sendGet(Endpoint.Queries.LIST_BROWSE_QUERIES, param);
        return this;
    }

    @Step("Check response when hashtags does not exist")
    public void checkLResponseWhenDataNull(SearchTagsResponse searchTagsResponse) {
        validateResponse(HttpStatus.SC_OK);
        Assert.assertTrue(searchTagsResponse.getData().length==0);
    }
}
