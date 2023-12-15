package microservices.querypublic.models;

import lombok.Data;

@Data
public class SaveQueryInput {
    private String queryId;
    public SaveQueryInput(String queryId){
        this.queryId = queryId;
    }
}
