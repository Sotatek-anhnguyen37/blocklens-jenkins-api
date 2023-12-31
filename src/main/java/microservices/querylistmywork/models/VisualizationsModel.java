package microservices.querylistmywork.models;

import lombok.Data;
import microservices.querydetail.models.OptionsInsertVisualModel;
import microservices.querypublic.models.QueryModel;

@Data
public class VisualizationsModel {
    private String id;
    private String type;
    private String name;
    private String queryId;
    private OptionsInsertVisualModel options;
    private String createdAt;
    private String updatedAt;
    private QueryModel query;
}
