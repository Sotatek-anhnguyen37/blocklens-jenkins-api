package microservices.dashboardmywork.models;

import lombok.Data;
import microservices.dashboardpublic.models.OptionsUpdateDashboardInput;

@Data
public class DashboardVisualsModel {
    private String id;
    private String visualizationId;
    private String dashboardId;
    private String userId;
    private OptionsUpdateDashboardInput options;
    private String createdAt;
    private String updatedAt;
    private Object visualization;
}
