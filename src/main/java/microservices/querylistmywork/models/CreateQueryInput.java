package microservices.querylistmywork.models;

import lombok.Data;

@Data
public class CreateQueryInput {
    private String id;
    private String name;
    private String query;
    private String[] tags;
    public CreateQueryInput(String id, String name, String query, String[] tags){
        this.id = id;
        this.name = name;
        this.query = query;
        this.tags = tags;
    }
}
