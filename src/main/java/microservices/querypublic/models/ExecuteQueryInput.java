package microservices.querypublic.models;

import lombok.Data;

@Data
public class ExecuteQueryInput {
    private String id;
    private String statement;

    public ExecuteQueryInput(String id, String statement){
        this.id = id;
        this.statement = statement;
    }
}
