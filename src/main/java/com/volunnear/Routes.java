package com.volunnear;

public class Routes {

    private static final String API_URL = "/api/v1";

    /**
     * Users registration and login
     */
    private static final String AUTH = API_URL + "/auth";
    public static final String LOGIN = AUTH + "/login";
    public static final String REFRESH_TOKEN = AUTH + "/refresh-token";
    public static final String REGISTER = AUTH + "/register";

    public static final String REGISTER_VOLUNTEER = REGISTER + "/volunteer";
    public static final String REGISTER_ORGANIZATION = REGISTER + "/organization";
    public static final String UPDATE_VOLUNTEER_PROFILE = API_URL + "/update/volunteer";
    public static final String UPDATE_ORGANIZATION_PROFILE = API_URL + "/update/organization";
    /**
     * Organization routes
     */
    public static final String ORGANIZATION = API_URL + "/organization";
    public static final String GET_ORGANIZATION_ID = ORGANIZATION + "/my-id";
    public static final String GET_ALL_ORGANIZATIONS = ORGANIZATION + "/get-all";
    public static final String GET_ORGANIZATION_PROFILE = ORGANIZATION + "/my-profile";
    public static final String DELETE_ORGANIZATION_PROFILE = ORGANIZATION + "/delete-profile";
    public static final String APPROVE_VOLUNTEER_TO_ACTIVITY = ORGANIZATION + "/approve-volunteer";
    public static final String KICK_VOLUNTEER_FORM_ACTIVITY = ORGANIZATION + "/kick-volunteer";
    public static final String GET_ORGANIZATION_ACTIVITY_REQUESTS = ORGANIZATION + "/my-requests";
    public static final String GET_MY_ACTIVITIES_FOR_ORGANIZATION = ORGANIZATION + "/my-activities";

    /**
     * Volunteers routes
     */
    public static final String VOLUNTEER = API_URL + "/volunteer";
    public static final String GET_VOLUNTEER_PROFILE = VOLUNTEER + "/my-profile";
    public static final String JOIN_TO_ACTIVITY_REQUEST = VOLUNTEER + "/join-to-activity";
    public static final String DELETE_MY_JOIN_ACTIVITY_REQUEST = VOLUNTEER + "/delete-my-activity-request";
    public static final String DELETE_VOLUNTEER_PROFILE = VOLUNTEER + "/delete-profile";
    public static final String LEAVE_FROM_ACTIVITY_BY_VOLUNTEER = VOLUNTEER + "/leave-activity";
    public static final String GET_VOLUNTEER_ACTIVITY_REQUESTS = VOLUNTEER + "/my-requests";

    /**
     * Activity routes
     */
    public static final String GET_ACTIVITY_INFO = API_URL + "/activity-info";
    public static final String GET_ALL_ACTIVITIES = API_URL + "/get-all-activities";
    public static final String GET_VOLUNTEER_ACTIVITY_REQUEST_STATUS_INFO = API_URL + "/activity-request-status";

    public static final String ADD_ACTIVITY = ORGANIZATION + "/add-activity";
    public static final String GET_MY_ACTIVITIES = ORGANIZATION + "/my-activities";
    public static final String UPDATE_ACTIVITY_INFORMATION = ORGANIZATION + "/update-activity";
    public static final String ACTIVITY_CURRENT_ORGANIZATION = ORGANIZATION + "/get-activities";
    public static final String DELETE_CURRENT_ACTIVITY_BY_ID = ORGANIZATION + "/delete-activity";
    public static final String GET_VOLUNTEERS_FROM_CURRENT_ACTIVITY = API_URL + "/activity/volunteers-list";

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
    public static final String NOTIFICATIONS = API_URL + "/notification";
    public static final String GET_NOTIFICATION_SUBSCRIPTION_STATUS = NOTIFICATIONS + "/status";
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

    /**
     * Preferences routes
     */
    public static final String PREFERENCES = API_URL + "/preferences";
    public static final String ADD_PREFERENCE_TO_VOLUNTEER = PREFERENCES + "/add";
    public static final String UPDATE_PREFERENCE_TO_VOLUNTEER = PREFERENCES + "/update";

    public static final String GET_RECOMMENDATION_BY_PREFERENCES = VOLUNTEER + "/recommendations";

    public static final String[] SWAGGER_ENDPOINTS = {
            "api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
