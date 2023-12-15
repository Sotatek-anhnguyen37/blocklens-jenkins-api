package microservices.querypublic.models;

import lombok.Data;

@Data
public class ListQueryPublicModel {
    private DataQueryModel[] data;
    private int itemCount;
    private int totalItems;
    private int currentPage;
    private int itemsPerPage;
    private int totalPages;
}
