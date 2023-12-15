package microservices.dashboardpublic.models;

import lombok.Data;

@Data
public class SaveADashboardInput {
    private String dashboardId;
    public SaveADashboardInput(String dashboardId){
        this.dashboardId = dashboardId;
    }
}
