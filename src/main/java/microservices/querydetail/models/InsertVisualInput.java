package microservices.querydetail.models;

import lombok.Data;

@Data
public class InsertVisualInput {
    private String queryId;
    private String type;
    private String name;
    private OptionsInsertVisualModel options;

    public InsertVisualInput(String queryId, String type, String name, OptionsInsertVisualModel options){
        this.queryId = queryId;
        this.type = type;
        this.name = name;
        this.options = options;
    }
}
