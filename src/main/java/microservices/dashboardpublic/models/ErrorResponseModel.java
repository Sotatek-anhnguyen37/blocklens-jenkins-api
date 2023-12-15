package microservices.dashboardpublic.models;

import lombok.Data;

@Data
public class ErrorResponseModel {
    private int statusCode;
    private String message;
}
