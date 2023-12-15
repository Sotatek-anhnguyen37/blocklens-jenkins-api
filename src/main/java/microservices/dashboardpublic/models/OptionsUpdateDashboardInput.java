package microservices.dashboardpublic.models;

import lombok.Data;
@Data
public class OptionsUpdateDashboardInput {
    private int col;
    private int row;
    private int sizeX;
    private int sizeY;

    public OptionsUpdateDashboardInput(){
        this.col = 0;
        this.row = 0;
        this.sizeX = 6;
        this.sizeY = 2;
    }
    public OptionsUpdateDashboardInput(int col, int row, int sizeX, int sizeY){
        this.col = col;
        this.row = row;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}
