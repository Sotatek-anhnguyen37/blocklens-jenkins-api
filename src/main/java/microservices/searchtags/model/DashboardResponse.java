package microservices.searchtags.model;

import lombok.Data;

import java.util.List;

@Data
public class DashboardResponse {
    private DataDashboard[] data;
    private int itemCount;
    private int totalItems;
    private int currentPage;
    private int itemsPerPage;
    private int totalPages;

    @Data
    public class DataDashboard {
        private String id;
        private String name;
        private boolean isPrivate;
        private boolean isArchived;
        private String userId;
        private String forkedDashboardId;
        private String thumbnail;
        private List<String> tags;
        private String createdAt;
        private String updatedAt;
        private Object[] dashboardVisuals;
        private Object[] textWidgets;
        private UserInfo userInfo;
        private long _createdAt;
        private long _updatedAt;
        private int viewCount;

        @Data
        public class UserInfo{
            private String _id;
            private String id;
            private String email;
            private String firstName;
            private String lastName;
        }
    }
}
