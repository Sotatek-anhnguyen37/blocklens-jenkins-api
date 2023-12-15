package microservices.dashboardpublic.models;

import lombok.Data;

import java.util.List;

@Data
public class UpdateDashboardInput {
    private String name;
    private String[] tags;
    private List<DashboardVisualsInput> dashboardVisuals;
    private List<TextWidgetsInput> textWidgets;

    public UpdateDashboardInput(String _name, String[] _tags, List<DashboardVisualsInput> visuals, List<TextWidgetsInput> widgets){
        this.name = _name;
        this.tags = _tags;
        this.dashboardVisuals = visuals;
        this.textWidgets = widgets;
    }
    public UpdateDashboardInput(String name, String[] tags){
        this.name = name;
        this.tags = tags;
    }
}
