package microservices.searchtags.model;

import lombok.Data;

@Data
public class SearchTagsResponse {
    private String[] data;
    private int itemCount;
    private int totalItems;
}
