package microservices.querylistmywork.models;

import lombok.Data;

@Data
public class ListBrowserQuery {
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
        private Object parameters;
        private String[] utilizedChains;
        private String[] tags;
        private int viewCount;
        private String forkedQueryName;
        private String lastUpdatedAt;
        private String createdAt;
        private String updatedAt;
        private VisualizationsModel[] visualizations;
        private UserInfo userInfo;
        @Data
        public class UserInfo{
            private String email;
            private String firstName;
            private String lastName;
        }
    }
}
