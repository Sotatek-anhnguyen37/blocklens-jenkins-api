package microservices.dashboardmywork.steps;

import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import microservices.dashboardmywork.models.FindMyDashboardModel;
import microservices.dashboardpublic.models.DashboardsListModel;
import microservices.dashboardpublic.models.UpdateDashboardInput;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DashboardMyWorkSteps extends BaseApi {
    @Step("create dashboard empty")
    public DashboardMyWorkSteps createDashboardEmpty(Object body){
        sendPost(Endpoint.Dashboards.POST_CREATE_DASHBOARD, body);
        return this;
    }
    @Step("find my dashboard after create dashboard")
    public DashboardMyWorkSteps findMyDashboard(Map<String, Object> params){
        sendGet(Endpoint.Dashboards.FIND_MY_DASHBOARD_BY_ID, params);
        return this;
    }
    @Step("filter dashboard save by Ids")
    public DashboardMyWorkSteps filterDashboardSave(Map<String, Object> params) {
        sendGet(Endpoint.DashboardSaved.FILTER_DASHBOARD_SAVE, params);
        return this;
    }
    @Step("add dashboard id into list")
    public List<String> addDashboardIdIntoList(DashboardsListModel dashboardsListModel){
        List<String> listIds = new ArrayList<>();
        for(DashboardsListModel.DataDashboard data : dashboardsListModel.getData()){
            listIds.add(data.getId());
        }
        return listIds;
    }
    @Step("delete dashboard")
    public DashboardMyWorkSteps deleteDashboard(String id){
        sendDelete(Endpoint.Dashboards.DELETE_ONE_DASHBOARD, "dashboardId", id );
        return this;
    }
    @Step("get list browser dashboards")
    public DashboardMyWorkSteps getListBrowseDashboard(Map<String, Object> params){
        sendGet(Endpoint.Dashboards.LIST_BROWSE_DASHBOARDS, params);
        return this;
    }
    @Step("check dashboard have into list dashboard my work")
    public void checkDashboardHaveIntoListDashboardMyWork(DashboardsListModel dashboard, String id){
        boolean temp = false;
        for(DashboardsListModel.DataDashboard data : dashboard.getData()){
            if(Objects.equals(data.getId(), id)){
                temp = true;
                break;
            }
        }
        Assert.assertTrue(temp);
    }
    @Step("check dashboard have name and tag correctly")
    public void checkDashboard(DashboardsListModel.DataDashboard data, String nameDashboard, String tag){
        Assert.assertEquals(data.getName(), nameDashboard);
        Assert.assertEquals(data.getTags()[0], tag);
    }
    @Step("get all public dashboards")
    public DashboardMyWorkSteps getAllPublicDashboards(Map<String,Object> params){
        sendGet(Endpoint.Dashboards.GET_ALL_PUBLIC_DASHBOARDS, params);
        return this;
    }
    @Step("save dashboard")
    public DashboardMyWorkSteps saveDashboard(Object body){
        sendPost(Endpoint.DashboardSaved.DASHBOARD_SAVE, body);
        return this;
    }
    @Step("get list save dashboard save")
    public DashboardMyWorkSteps getListDashboardSave(Map<String, Object> params){
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
    @Step("verify dashboard have not into list save dashboard")
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
    @Step("remove dashboard out save dashboard list")
    public DashboardMyWorkSteps removeSaveDashboardList(Object body){
        sendDelete(Endpoint.DashboardSaved.DASHBOARD_SAVE, body);
        return this;
    }
    @Step("update dashboard")
    public DashboardMyWorkSteps updateDashboard(UpdateDashboardInput updateDashboardInput, String dashboardId){
        sendPatch(Endpoint.Dashboards.UPDATE_DASHBOARD, updateDashboardInput, "dashboardId", dashboardId);
        return this;
    }
    @Step("verify dashboard have widget")
    public void verifyDashboardHaveWidget(FindMyDashboardModel findMyDashboardModel, String textWidget){
        Assert.assertTrue(findMyDashboardModel.getTextWidgets().length > 0);
        Assert.assertEquals(findMyDashboardModel.getTextWidgets()[0].getText(), textWidget);
    }
    @Step("get list visualization")
    public DashboardMyWorkSteps getListVisualization(){
        sendGet(Endpoint.Visualizations.GET_LIST_VISUALIZATIONS);
        return this;
    }
    @Step("verify dashboard have visualization")
    public void verifyDashboardHaveVisualization(FindMyDashboardModel dashboard, int numberVisuals){
        Assert.assertEquals(dashboard.getDashboardVisuals().length, numberVisuals);
    }
    @Step("check widget have removed out")
    public void checkWidgetHaveRemovedOut(FindMyDashboardModel findMyDashboardModel){
        Assert.assertEquals(findMyDashboardModel.getTextWidgets().length, 0);
    }
}
