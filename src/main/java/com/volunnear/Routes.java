package com.volunnear;

public class Routes {

    private static final String API_URL = "/api/v1";

    /**
     * Users management
     */
    public static final String LOGIN = API_URL + "/login";
    public static final String REFRESH_TOKEN = API_URL + "/refresh-token";
    public static final String REGISTER_ROUTE_SECURITY = API_URL + "/registration";
    public static final String REGISTER_VOLUNTEER = API_URL + "/registration/volunteer";
    public static final String REGISTER_ORGANIZATION = API_URL + "/registration/organization";
    public static final String UPDATE_VOLUNTEER_PROFILE = API_URL + "/update/volunteer";
    public static final String UPDATE_ORGANIZATION_PROFILE = API_URL + "/update/organization";
    /**
     * Organization routes
     */
    public static final String ORGANIZATION = API_URL + "/organization";
    public static final String GET_ALL_ORGANIZATIONS = ORGANIZATION + "/get-all";
    public static final String GET_ORGANIZATION_PROFILE = ORGANIZATION + "/my-profile";
    public static final String DELETE_ORGANIZATION_PROFILE = ORGANIZATION + "/delete-profile";

    /**
     * Volunteers routes
     */
    public static final String VOLUNTEER = API_URL + "/volunteer";
    public static final String GET_VOLUNTEER_PROFILE = VOLUNTEER + "/my-profile";
    public static final String JOIN_TO_ACTIVITY = VOLUNTEER + "/join-to-activity";
    public static final String DELETE_VOLUNTEER_PROFILE = VOLUNTEER + "/delete-profile";
    public static final String DELETE_PREFERENCE_BY_ID = VOLUNTEER + "/delete-preference";
    public static final String SET_VOLUNTEERS_PREFERENCES = VOLUNTEER + "/set-preferences";
    public static final String LEAVE_FROM_ACTIVITY_BY_VOLUNTEER = VOLUNTEER + "/leave-activity";
    public static final String GET_RECOMMENDATION_BY_PREFERENCES = VOLUNTEER + "/recommendations";

    /**
     * Activity routes
     */
    public static final String GET_ACTIVITY_INFO = API_URL + "/activity-info";
    public static final String GET_ALL_ACTIVITIES = API_URL + "/get-all-activities";

    public static final String ADD_ACTIVITY = ORGANIZATION + "/add-activity";
    public static final String GET_MY_ACTIVITIES = ORGANIZATION + "/my-activities";
    public static final String UPDATE_ACTIVITY_INFORMATION = ORGANIZATION + "/update-activity";
    public static final String ACTIVITY_CURRENT_ORGANIZATION = ORGANIZATION + "/get-activities";
    public static final String DELETE_CURRENT_ACTIVITY_BY_ID = ORGANIZATION + "/delete-activity";

    /**
     * Feedback routes
     */
    public static final String FEEDBACK = API_URL + "/feedback";
    public static final String POST_FEEDBACK_ABOUT_ORGANIZATION = FEEDBACK + "/give-feedback";
    public static final String GET_FEEDBACKS_OF_ALL_ORGANIZATIONS = FEEDBACK + "/get-all";
    public static final String UPDATE_FEEDBACK_FOR_CURRENT_ORGANIZATION = FEEDBACK + "/update-feedback";
    public static final String GET_FEEDBACKS_FROM_CURRENT_ORGANIZATION = FEEDBACK + "/feedbacks-of-organization";
    public static final String DELETE_FEEDBACK_ABOUT_ORGANIZATION = FEEDBACK + "/remove-feedback";
    /**
     * Location routes
     */
    public static final String LOCATION = API_URL + "/location";
    public static final String FIND_NEARBY_ACTIVITIES = LOCATION + "/find-nearby";
    /**
     * Notifications routes
     */
    public static final String NOTIFICATIONS = API_URL + "/notifications";
    public static final String GET_ALL_SUBSCRIPTIONS_OF_VOLUNTEER = NOTIFICATIONS + "/my-subscriptions";
    public static final String SUBSCRIBE_TO_NOTIFICATIONS_BY_ID_OF_ORGANIZATION = NOTIFICATIONS + "/subscribe";
    public static final String UNSUBSCRIBE_FROM_NOTIFICATIONS_BY_ID_OF_ORGANIZATION = NOTIFICATIONS + "/unsubscribe";
    /**
     * Social networks integration routes
     */
    public static final String SOCIAL_NETWORKS = API_URL + "/social";
    public static final String ADD_COMMUNITY_LINK = SOCIAL_NETWORKS + "/add-community-link";
    public static final String GET_CHAT_LINK_BY_ACTIVITY = SOCIAL_NETWORKS + "/get-chat-link";
    public static final String ADD_CHAT_LINK_FOR_ACTIVITY = SOCIAL_NETWORKS + "/add-chat-link";
    public static final String GET_COMMUNITY_LINK_BY_ORGANIZATION = SOCIAL_NETWORKS + "/get-community-link";

    public static final String[] SWAGGER_ENDPOINTS = {
            "api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
