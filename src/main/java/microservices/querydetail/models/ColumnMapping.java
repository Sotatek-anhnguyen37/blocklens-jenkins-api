package microservices.querydetail.models;

import lombok.Data;

@Data
public class ColumnMapping {
    private String xaxis;
    private String[] yaxis;
    public ColumnMapping(){
        this.xaxis = "";
        this.yaxis = new String[]{};
    }
    public ColumnMapping(String xAxis, String yAxis){
        this.xaxis = xAxis;
        this.yaxis = new String[]{yAxis};
    }
}
