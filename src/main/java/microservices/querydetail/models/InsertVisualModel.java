package microservices.querydetail.models;

import lombok.Data;
import microservices.querypublic.models.QueryModel;

@Data
public class InsertVisualModel {
    private String id;
    private QueryModel query;
    private String queryId;
    private String type;
    private String name;
    private OptionsInsertVisualModel options;
    private String createdAt;
    private String updatedAt;
}
