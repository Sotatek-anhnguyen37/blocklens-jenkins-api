package microservices.dashboardpublic.steps;

import constants.DateFormats;
import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import microservices.dashboardpublic.constants.DashboardsConstants;
import microservices.dashboardpublic.models.*;
import microservices.signin.model.SignInResponse;
import org.testng.Assert;
import util.CommonUtil;

import java.net.HttpURLConnection;
import java.util.*;

public class DashboardPublicSteps extends BaseApi {
    private SignInResponse signInResponse;
    @Step("sign in")
    public DashboardPublicSteps when_signIn(Object body) {
        sendPost(Endpoint.BlocklensUsers.SIGN_IN_URL, body);
        signInResponse = (SignInResponse) saveResponseObject(SignInResponse.class);
        removeHeader("Authorization");
        setHeader("Authorization", "Bearer "+ signInResponse.getAccessToken());
        return this;
    }
    @Step("get all public dashboards")
    public DashboardPublicSteps getAllPublicDashboards(Map<String,Object> params){
        sendGet(Endpoint.Dashboards.GET_ALL_PUBLIC_DASHBOARDS, params);
        return this;
    }
    @Step("verify response success of list dashboards")
    public void verifyResponseListDashboard(DashboardsListModel dashboardsListModel, Map<String, Object> params){
        for(DashboardsListModel.DataDashboard data : dashboardsListModel.getData()){
            Assert.assertNotNull(data.getId());
            Assert.assertNotNull(data.getName());
            Assert.assertFalse(data.isPrivate());
            Assert.assertTrue(data.isArchived());
            Assert.assertNotNull(data.getUserId());
            Assert.assertNotNull(data.getTags());
            Assert.assertNotNull(data.getUserInfo().getEmail());
            Assert.assertNotNull(data.getUserInfo().getFirstName());
            Assert.assertNotNull(data.getUserInfo().getLastName());
            Assert.assertEquals(data.get_createdAt(), CommonUtil.convertDateToMilliseconds(data.getCreatedAt(), DateFormats.DATE_ISO_8601));
            Assert.assertEquals(data.get_updatedAt(), CommonUtil.convertDateToMilliseconds(data.getUpdatedAt(), DateFormats.DATE_ISO_8601));
        }
        Assert.assertEquals(dashboardsListModel.getItemCount(), dashboardsListModel.getData().length);
        Assert.assertEquals(dashboardsListModel.getCurrentPage(), params.get("page"));
    }
    @Step("verify response bad request of get list dashboard public")
    public void verifyResponseBadRequestListDashboard(ErrorMessageModel errorMessageModel){
        Assert.assertEquals(errorMessageModel.getStatusCode(), HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(errorMessageModel.getMessage()[0], DashboardsConstants.MESSAGE_ERROR_PAGE_AND_LIMIT);
        Assert.assertEquals(errorMessageModel.getError(), DashboardsConstants.MESSAGE_ERROR_PAGE_AND_LIMIT);
    }
    @Step("get trending dashboard tags")
    public DashboardPublicSteps getTrendingDashboardTags(){
        sendGet(Endpoint.Dashboards.GET_TAGS_TRENDING);
        return this;
    }
    @Step("verify response of trending dashboard tags")
    public void verifyResponseOfTrendingDashboardTags(TagsModel tagsModel){
        Assert.assertTrue(tagsModel.getTags().length <= 10);
    }
    @Step("verify response bad request of get list dashboard public params empty")
    public void verifyResponseBadRequestListDashboardParamsEmpty(ErrorMessageModel errorMessageModel){
        Assert.assertEquals(errorMessageModel.getStatusCode(), HttpURLConnection.HTTP_BAD_REQUEST);
        Assert.assertEquals(errorMessageModel.getMessage()[0], DashboardsConstants.MESSAGE_ERROR_EMPTY);
        Assert.assertEquals(errorMessageModel.getError(), DashboardsConstants.BAD_REQUEST);
    }
    @Step("check response of list dashboards has searchName")
    public void checkListDashboardHasSearchName(DashboardsListModel dashboardsListModel, Object searchName){
        for(DashboardsListModel.DataDashboard data : dashboardsListModel.getData()){
            Assert.assertTrue(data.getName().toUpperCase().contains(searchName.toString().trim().toUpperCase()));
        }
    }
    @Step("check list dashboards sorted by created at")
    public void checkListDashboardsSortedByCreatedAt(DashboardsListModel dashboardsListModel, String orderBy){
        if(orderBy.toUpperCase().contains(DashboardsConstants.ORDER_BY[0])){
            for(int i = 1; i<dashboardsListModel.getData().length; i++){
                Assert.assertTrue(dashboardsListModel.getData()[i].get_createdAt() >= dashboardsListModel.getData()[i-1].get_createdAt());
            }
        }
        else{
            for(int i = 1; i<dashboardsListModel.getData().length; i++){
                Assert.assertTrue(dashboardsListModel.getData()[i].get_createdAt() <= dashboardsListModel.getData()[i-1].get_createdAt());
            }
        }
    }
    @Step("verify response error when sorted by update at")
    public void verifyResponseErrorSortedByUpdateAt(ErrorResponseModel errorResponseModel){
        Assert.assertTrue(errorResponseModel.getMessage().toUpperCase().contains(DashboardsConstants.INVALID_ORDER.toUpperCase()));
        Assert.assertEquals(errorResponseModel.getStatusCode(), HttpURLConnection.HTTP_BAD_REQUEST);
    }
    @Step("verify list dashboard empty")
    public void verifyListDashboardsEmpty(DashboardsListModel dashboardsListModel){
        Assert.assertEquals(dashboardsListModel.getData().length, 0);
    }
    @Step("create dashboard empty")
    public DashboardPublicSteps createDashboardEmpty(Object body){
        sendPost(Endpoint.Dashboards.POST_CREATE_DASHBOARD, body);
        validateResponse(HttpURLConnection.HTTP_CREATED);
        return this;
    }
    @Step("delete one dashboard")
    public DashboardPublicSteps deleteOneDashboard(String dashboardId){
        sendDelete(Endpoint.Dashboards.DELETE_ONE_DASHBOARD, "dashboardId", dashboardId);
        return this;
    }
    @Step("find my dashboard by id")
    public DashboardPublicSteps findMyDashboardById(Map<String, Object> params){
        sendGet(Endpoint.Dashboards.FIND_MY_DASHBOARD_BY_ID, params);
        return this;
    }
    @Step("get dashboard detail by id")
    public DashboardPublicSteps getDashboardDetail(String dashboardId){
        sendGet(Endpoint.Dashboards.GET_DASHBOARD_DETAIL, "dashboardId", dashboardId);
        return this;
    }
    @Step("verify dashboard have visualization")
    public void verifyDashboardHaveVisualization(DashboardsListModel.DataDashboard dashboard){
        Assert.assertTrue(dashboard.getDashboardVisuals().length > 0);
    }
    @Step("verify dashboard have not widget and visual")
    public void verifyDashboardHaveNotWidgetAndVisual(DashboardsListModel.DataDashboard dashboard){
        Assert.assertEquals(dashboard.getDashboardVisuals().length, 0);
        Assert.assertEquals(dashboard.getTextWidgets().length, 0);
    }
    @Step("verify dashboard have widget")
    public void verifyDashboardHaveWidget(DashboardsListModel.DataDashboard dashboard){
        Assert.assertTrue(dashboard.getTextWidgets().length > 0);
    }
    @Step("verify dashboard have widget and visual")
    public void verifyDashboardHaveWidgetAndVisual(DashboardsListModel.DataDashboard dashboard){
        Assert.assertTrue(dashboard.getTextWidgets().length > 0);
        Assert.assertTrue(dashboard.getDashboardVisuals().length > 0);
    }
    @Step("check dashboard before added visualization and widget")
    public List<Integer> verifyDashboardBeforeAddVisualizationAndWidget(DashboardsListModel.DataDashboard data){
        List<Integer> numberVisualAndWidget = new ArrayList<>();
        numberVisualAndWidget.add(data.getDashboardVisuals().length);
        numberVisualAndWidget.add(data.getTextWidgets().length);
        return numberVisualAndWidget;
    }
    @Step("check dashboard after added visualization and widget")
    public void verifyDashboardAfterAddVisualizationAndWidget(DashboardsListModel.DataDashboard data, List<Integer> number){
        Assert.assertNotEquals(data.getTextWidgets().length, number.get(1));
    }
    @Step("update information of dashboard")
    public DashboardPublicSteps updateDashboard(UpdateDashboardInput updateDashboardInput, String dashboardId){
        sendPatch(Endpoint.Dashboards.UPDATE_DASHBOARD, updateDashboardInput, "dashboardId", dashboardId);
        return this;
    }
    @Step("get list visualization")
    public DashboardPublicSteps getListVisualization(){
        sendGet(Endpoint.Visualizations.GET_LIST_VISUALIZATIONS);
        return this;
    }
    @Step("save dashboard")
    public DashboardPublicSteps saveDashboard(Object body){
        sendPost(Endpoint.DashboardSaved.DASHBOARD_SAVE, body);
        return this;
    }
    @Step("remove dashboard out save dashboard list")
    public DashboardPublicSteps removeSaveDashboardList(Object body){
        sendDelete(Endpoint.DashboardSaved.DASHBOARD_SAVE, body);
        return this;
    }
    @Step("get list save dashboard save")
    public DashboardPublicSteps getListSaveDashboardSave(Map<String, Object> params){
        sendGet(Endpoint.DashboardSaved.DASHBOARD_SAVE, params);
        return this;
    }
    @Step("verify dashboard have into list save dashboard")
    public void verifyDashboardHaveIntoListSaveDashboard(DashboardsListModel dashboard, String dashboardId){
        boolean temp = false;
        for(DashboardsListModel.DataDashboard data : dashboard.getData()){
            if (Objects.equals(data.getId(), dashboardId)) {
                temp = true;
                break;
            }
        }
        Assert.assertTrue(temp);
    }
    @Step("verify dashboard have not into list save dashboard yet")
    public void verifyDashboardHaveNotIntoListSaveDashboard(DashboardsListModel dashboard, String dashboardId){
        boolean temp = false;
        for(DashboardsListModel.DataDashboard data : dashboard.getData()){
            if (Objects.equals(data.getId(), dashboardId)) {
                temp = true;
                break;
            }
        }
        Assert.assertFalse(temp);
    }
    @Step("verify dashboard is not owner")
    public void verifyDashboardIsNotOwner(DashboardsListModel.DataDashboard data){
        Assert.assertNull(data.getUserInfo().get_id());
        Assert.assertNull(data.getUserInfo().getId());
    }
    @Step("verify dashboard is owner")
    public void verifyDashboardIsOwner(DashboardsListModel.DataDashboard data){
        Assert.assertNotNull(data.getUserInfo().get_id());
        Assert.assertNotNull(data.getUserInfo().getId());
    }
    @Step("Create dashboard")
    public DashboardPublicSteps createDashboard(Object body){
        sendPost(Endpoint.Dashboards.POST_CREATE_DASHBOARD, body);
        validateResponse(HttpURLConnection.HTTP_CREATED);
        return this;
    }
}
