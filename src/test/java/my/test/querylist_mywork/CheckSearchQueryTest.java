package my.test.querylist_mywork;

import core.BaseTest;
import io.qameta.allure.Feature;
import microservices.accountScreen.constants.AccountScreenConstants;
import microservices.accountScreen.steps.AccountScreenSteps;
import microservices.querylistmywork.models.ListBrowserQuery;
import microservices.querylistmywork.steps.QueryMyWorkSteps;
import microservices.signin.model.SignInInput;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Feature("Queries List My Work")
public class CheckSearchQueryTest extends BaseTest {
    private QueryMyWorkSteps querySteps = new QueryMyWorkSteps();
    private AccountScreenSteps accountScreenSteps = new AccountScreenSteps();
    private ListBrowserQuery list;
    @BeforeMethod(alwaysRun = true)
    public void signIn(){
        accountScreenSteps.when_signIn(new SignInInput(AccountScreenConstants.EMAIL, AccountScreenConstants.PASS_WORD));
    }
    @Test(description = "get list query in my work by name exist and check")
    public void getListQueriesBySearchName(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", "testDelete");
        list = (ListBrowserQuery) querySteps.getQueryListInMyWork(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListBrowserQuery.class);
        querySteps.verifyNameQueryAfterSearch(list, "testDelete");
    }

    @Test(description = "get list query in my work by name not exist and check")
    public void getListQueriesBySearchNameNotExist(){
        Map<String, Object> params = new HashMap<>();
        params.put("search", "testDelete123");
        list = (ListBrowserQuery) querySteps.getQueryListInMyWork(params).validateStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .saveResponseObject(ListBrowserQuery.class);
        querySteps.verifyNameQueryAfterSearchNotExist(list);
    }

    @Test(description = "get query search by multiple tags exist and check")
    public void getQuerySearchByMultipleTagsExist(){
        Map<String, Object> params = new HashMap<>();
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        params.put("tags", tags);
        list = (ListBrowserQuery) querySteps.getQueryListInMyWork(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListBrowserQuery.class);
        querySteps.checkQueryHaveMultipleTagsExist(list);
    }

    @Test(description = "get query search by multiple tags not exist and check")
    public void getQuerySearchByMultipleTagsNotExist(){
        Map<String, Object> params = new HashMap<>();
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("abcde");
        params.put("tags", tags);
        list = (ListBrowserQuery) querySteps.getQueryListInMyWork(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListBrowserQuery.class);
        querySteps.verifyNameQueryAfterSearchNotExist(list);
    }

    @Test(description = "check queries are sorted by ascending and check")
    public void checkQueriesSortedAsc(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:asc");
        list = (ListBrowserQuery) querySteps.getQueryListInMyWork(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListBrowserQuery.class);
        querySteps.checkQueriesSorted(list, params);
    }
    @Test(description = "check queries are sorted by descending and check")
    public void checkQueriesSortedDesc(){
        Map<String, Object> params = new HashMap<>();
        params.put("orderBy", "created_at:desc");
        list = (ListBrowserQuery) querySteps.getQueryListInMyWork(params).validateStatusCode(HttpURLConnection.HTTP_OK)
                .saveResponseObject(ListBrowserQuery.class);
        querySteps.checkQueriesSorted(list, params);
    }
}
