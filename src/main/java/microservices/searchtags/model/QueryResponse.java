package microservices.searchtags.model;

import lombok.Data;
import microservices.querylistmywork.models.VisualizationsModel;

import java.util.List;

@Data
public class QueryResponse {
    private int itemCount;
    private int totalItems;
    private int currentPage;
    private int itemsPerPage;
    private int totalPages;
    private QueryData[] data;
    @Data
    public class QueryData{
        private String id;
        private String name;
        private boolean isPrivate;
        private boolean isArchived;
        private String forkedQueryId;
        private String userId;
        private String query;
        private String executedId;
        private String pendingExecutionId;
        private List<String> tags;
        private int viewCount;

        private String createdAt;
        private String updatedAt;
        private VisualizationsModel[] visualizations;
    }
}
