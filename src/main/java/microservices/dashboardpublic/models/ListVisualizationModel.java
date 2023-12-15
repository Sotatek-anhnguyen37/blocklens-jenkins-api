package microservices.dashboardpublic.models;

import lombok.Data;

@Data
public class ListVisualizationModel {
    private int itemCount;
    private int totalItems;
    private int currentPage;
    private int itemsPerPage;
    private int totalPages;
    private DataVisualization[] data;

    @Data
    public class DataVisualization{
        private String id;
        private String type;
        private String name;
        private String queryId;
        private Object options;
        private String createdAt;
        private String updatedAt;
        private Object query;
    }
}
