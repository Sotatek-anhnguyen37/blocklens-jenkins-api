package microservices.querydetail.steps;

import constants.Endpoint;
import core.BaseApi;
import io.qameta.allure.Step;
import microservices.querydetail.models.EditVisualInput;
import microservices.querydetail.models.InsertVisualModel;
import microservices.querylistmywork.models.ListBrowserQuery;
import microservices.querylistmywork.models.VisualizationsModel;
import org.testng.Assert;

import java.util.Objects;

public class QueryDetailSteps extends BaseApi {
    @Step("insert visual")
    public QueryDetailSteps insertVisual(Object body){
        sendPost(Endpoint.Visualizations.INSERT_A_VISUAL, body);
        return this;
    }
    @Step("edit a visual")
    public QueryDetailSteps editAVisual(EditVisualInput editVisualInput, String visualId){
        sendPatch(Endpoint.Visualizations.EDIT_VISUAL, editVisualInput, "visualId", visualId);
        return this;
    }
    @Step("delete a visual")
    public QueryDetailSteps deleteVisual(String visualId){
        sendDelete(Endpoint.Visualizations.DELETE_A_VISUAL, "visualId", visualId);
        return this;
    }
    @Step("check query inserted visual")
    public void checkQueryInsertedVisual(ListBrowserQuery.QueryData query, String visualId, String nameChart){
        for(VisualizationsModel visuals : query.getVisualizations()){
            if(Objects.equals(visuals.getId(), visualId)){
                Assert.assertEquals(visuals.getName(), nameChart);
            }
        }
    }
    @Step("check visual of query edited name success")
    public void checkVisualEditedName(ListBrowserQuery.QueryData queryData, String newName){
        boolean temp = false;
        for(VisualizationsModel visuals : queryData.getVisualizations()){
            if(Objects.equals(visuals.getName(), newName)){
                temp = true;
                break;
            }
        }
        Assert.assertTrue(temp);
    }
    @Step("check visual of query edited option success")
    public void checkVisualEditedOptions(ListBrowserQuery.QueryData queryData, String visualId){
        for(VisualizationsModel visuals : queryData.getVisualizations()){
            if(Objects.equals(visuals.getId(), visualId)){
                Assert.assertEquals(visuals.getOptions().getColumnMapping().getXaxis(), "hash");
                Assert.assertEquals(visuals.getOptions().getColumnMapping().getYaxis()[0], "number");
            }
        }
    }
    @Step("delete all of visual of query")
    public void deleteAllOfVisualsOfQuery(ListBrowserQuery.QueryData dataQuery){

        for(VisualizationsModel visual : dataQuery.getVisualizations()){
            deleteVisual(visual.getId());
        }
    }
}
