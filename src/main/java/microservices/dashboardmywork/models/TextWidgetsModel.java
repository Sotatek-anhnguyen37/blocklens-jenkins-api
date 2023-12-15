package microservices.dashboardmywork.models;

import lombok.Data;
import microservices.dashboardpublic.models.OptionsUpdateDashboardInput;

@Data
public class TextWidgetsModel {
    private String id;
    private String dashboardId;
    private String text;
    private OptionsUpdateDashboardInput options;
    private String createdAt;
    private String updateAt;
}
