package microservices.querypublic.models;

import lombok.Data;

@Data
public class ForkQueryInput {
    private String newQueryName;
    public ForkQueryInput(String newQueryName){
        this.newQueryName = newQueryName;
    }
}
