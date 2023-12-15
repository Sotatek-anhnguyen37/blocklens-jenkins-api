package microservices.querypublic.models;

import lombok.Data;

@Data
public class DataQueryModel {
    private String id;
    private String name;
    private boolean isPrivate;
    private boolean isArchived;
    private String forkedQueryId;
    private String userId;
    private String query;
    private String executedId;
    private String[] tags;
    private String createdAt;
    private String updatedAt;
    private UserInfo userInfo;
    private long _createdAt;
    private long _updatedAt;
    private int viewCount;

    @Data
    public class UserInfo{
        private String email;
        private String firstName;
        private String lastName;
    }
}
