package microservices.querypublic.models;

import lombok.Data;

@Data
public class ExecuteQueryModel {
    private String id;
    private String queryId;
    private String jobId;
    private String queryStatement;
    private String status;
    private Object result;
    private ErrorModel error;
    private String createdAt;
    private String updatedAt;
    @Data
    public class ErrorModel{
        private String name;
        private String mesage;
        private Object metadata;
    }
}
