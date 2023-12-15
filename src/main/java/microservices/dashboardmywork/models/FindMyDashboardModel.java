package microservices.dashboardmywork.models;

import lombok.Data;

@Data
public class FindMyDashboardModel {
    private String id;
    private String name;
    private boolean isPrivate;
    private boolean isArchived;
    private String[] tags;
    private DashboardVisualsModel[] dashboardVisuals;
    private TextWidgetsModel[] textWidgets;
    private Object userInfo;
}
