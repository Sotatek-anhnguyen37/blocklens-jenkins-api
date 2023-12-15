package microservices.dashboardpublic.models;

import lombok.Data;

@Data
public class TextWidgetsInput {
    private String text;
    private OptionsUpdateDashboardInput options;
    public TextWidgetsInput(String _text, OptionsUpdateDashboardInput _option){
        this.text = _text;
        this.options = _option;
    }
}
