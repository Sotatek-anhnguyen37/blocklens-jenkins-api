package microservices.querydetail.models;

import lombok.Data;

@Data
public class OptionsInsertVisualModel {
    private String globalSeriesType;
    private ColumnMapping columnMapping;
    private ChartOptionsConfigs chartOptionsConfigs;

    @Data
    public class ChartOptionsConfigs{
        private boolean showLegend;
        public ChartOptionsConfigs(){
            this.showLegend = true;
        }
    }
    public OptionsInsertVisualModel(){}
    public OptionsInsertVisualModel(String globalSeriesType){
        this.globalSeriesType = globalSeriesType;
        this.chartOptionsConfigs = new ChartOptionsConfigs();
        this.columnMapping = new ColumnMapping();
    }
    public OptionsInsertVisualModel(ColumnMapping columnMapping, String globalSeriesType){
        this.columnMapping = columnMapping;
        this.globalSeriesType = globalSeriesType;
        this. chartOptionsConfigs = new ChartOptionsConfigs();
    }
}
