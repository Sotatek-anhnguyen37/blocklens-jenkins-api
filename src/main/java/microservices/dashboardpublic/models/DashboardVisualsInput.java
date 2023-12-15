package microservices.dashboardpublic.models;

import lombok.Data;

@Data
public class DashboardVisualsInput {
    private String visualizationId;
    private OptionsUpdateDashboardInput options;

    public DashboardVisualsInput(String _visualId, OptionsUpdateDashboardInput _option){
        this.visualizationId = _visualId;
        this.options = _option;
    }
}
