package microservices.dashboardpublic.models;

import lombok.Data;

@Data
public class ErrorMessageModel {
    private int statusCode;
    private String[] message;
    private String error;
}
