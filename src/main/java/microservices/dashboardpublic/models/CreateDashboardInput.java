package microservices.dashboardpublic.models;

import lombok.Data;

@Data
public class CreateDashboardInput {
    private String name;
    private boolean isPrivate;
    private String[] tags;

    public CreateDashboardInput(String _name, boolean _isPrivate, String[] _tags){
        this.name = _name;
        this.isPrivate = _isPrivate;
        this.tags = _tags;
    }
}
