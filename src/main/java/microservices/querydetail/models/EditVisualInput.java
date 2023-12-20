package microservices.querydetail.models;

import lombok.Data;

@Data
public class EditVisualInput {
    private String queryId;
    private String name;
    private OptionsInsertVisualModel options;
    public EditVisualInput(){}
    public EditVisualInput(String queryId, String name, OptionsInsertVisualModel options){
        this.queryId = queryId;
        this.name = name;
        this.options = options;
    }
}
