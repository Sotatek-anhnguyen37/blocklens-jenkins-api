package constants;

import static constants.Url.BASE_URL;

public class Endpoint {
    public static class BlocklensUsers {
        public static final String BASE_URL_BLOCLEN_USERS = BASE_URL + "/api/blocklens-users";
        public static final String SIGN_IN_URL = BASE_URL_BLOCLEN_USERS + "/users/signin";

        public static final String SIGN_UP_URL = BASE_URL_BLOCLEN_USERS + "/users/signup";
        public static final String VERIFY_EMAIL_URL = BASE_URL_BLOCLEN_USERS + "/users/verify-email";

        public static final String RESEND_EMAIL_URL = BASE_URL_BLOCLEN_USERS + "/users/resend-email";

        public static final String FORGOT_PASS_WORK = BASE_URL_BLOCLEN_USERS + "/users/forgot-password";
        public static final String RESET_PASS_WORK = BASE_URL_BLOCLEN_USERS + "/users/reset-password";


        public static final String GET_PROFILE_USER = BASE_URL + "/api/blocklens-users/users/my/profile";
        public static final String USER_API_KEY = "/api/blocklens-users/users/my/api-key";
        public static final String UPDATE_NOTIFICATION_FLAG = "/api/blocklens-users/users/my/notification";
    }
    public static class Dashboards {
        public static final String GET_ALL_PUBLIC_DASHBOARDS = "/api/blocklens-query-executor/public/dashboards";
        public static final String GET_TAGS_TRENDING = "/api/blocklens-query-executor/public/dashboards/tags/trending";
        public static final String POST_CREATE_DASHBOARD = "/api/blocklens-query-executor/dashboards/create-dashboard";
        public static final String DELETE_ONE_DASHBOARD = "/api/blocklens-query-executor/dashboards/{dashboardId}";
        public static final String UPDATE_DASHBOARD = "/api/blocklens-query-executor/dashboards/{dashboardId}/update-dashboard";
        public static final String FIND_MY_DASHBOARD_BY_ID = "/api/blocklens-query-executor/dashboards/find-my-dashboard";
        public static final String LIST_BROWSE_DASHBOARDS  = "/api/blocklens-query-executor/dashboards/list-browse-dashboards";
        public static final String GET_DASHBOARD_DETAIL = "/api/blocklens-query-executor/public/dashboards/{dashboardId}";
        public static final String PUBLIC_GET_TAGS = "/api/blocklens-query-executor/public/dashboards/tags";
        public static final String SEARCH_MY_DASHBOARD_TAGS  = "/api/blocklens-query-executor/dashboards/tags";

    }
    public static class Visualizations{
        public static final String GET_LIST_VISUALIZATIONS = "/api/blocklens-query-executor/visualizations/list-my-queries-visualizations";
    }
    public static class QueryExecutors{
        public static final String EXECUTE_A_QUERY = "/api/blocklens-query-executor/query-executors/execute-query";
        public static final String CANCEL_A_QUERY_EXECUTION = "/api/blocklens-query-executor/query-executors/cancel-query/{executionId}";
        public static final String GET_AN_EXECUTION = "/api/blocklens-query-executor/query-executors/get-execution";
    }
    public static class DashboardSaved{
        public static final String FILTER_DASHBOARD_SAVE  = "/api/blocklens-query-executor/dashboard-saveds/filters";
        public static final String DASHBOARD_SAVE = "/api/blocklens-query-executor/dashboard-saveds";
    }
    public static class Queries{
        public static final String GET_MANY_SCHEMAS = "/api/blocklens-query-executor/schemas";
        public static final String GET_ALL_PUBLIC_QUERIES = "/api/blocklens-query-executor/public/queries";
        public static final String LIST_BROWSE_QUERIES = "/api/blocklens-query-executor/queries/list-browse-queries";
        public static final String DELETE_QUERY = "/api/blocklens-query-executor/queries/{queryId}";
        public static final String GET_TAGS_TRENDING = "/api/blocklens-query-executor/public/queries/tags/trending";
        public static final String GET_DETAIL_QUERY = "/api/blocklens-query-executor/public/queries/{queryId}";
        public static final String FIND_MY_QUERY = "/api/blocklens-query-executor/queries/find-my-query";
        public static final String FORK_A_QUERY = "/api/blocklens-query-executor/queries/fork-query/{queryId}";
        public static final String PUBLIC_SEARCH_QUERY_TAGS = "/api/blocklens-query-executor/public/queries/tags";
        public static final String GET_ALL_PUBLIC_QUERY_TAGS = "/api/blocklens-query-executor/public/queries";
        public static final String SEARCH_MY_QUERY_TAGS  = "/api/blocklens-query-executor/queries/tags";
        public static final String CREATE_A_NEW_QUERY = "/api/blocklens-query-executor/queries/create-query";
    }
    public static class QuerySaved{
        public static final String FILTER_QUERY_SAVE = "/api/blocklens-query-executor/query-saveds/filters";
        public static final String QUERY_SAVE = "/api/blocklens-query-executor/query-saveds";
    }
}
