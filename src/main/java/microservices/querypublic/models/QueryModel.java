package microservices.querypublic.models;

import lombok.Data;
import microservices.querylistmywork.models.VisualizationsModel;

@Data
public class QueryModel {
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
    private String lastUpdateAt;
    private String createdAt;
    private String updatedAt;
    private VisualizationsModel[] visualizations;
}
