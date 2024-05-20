package com.volunnear;

public class Routes {

    private static final String API_URL = "/api/v1";

    /**
     * Users management
     */
    public static final String LOGIN = API_URL + "/login";
    public static final String REGISTER_ROUTE_SECURITY = API_URL + "/registration";
    public static final String REGISTER_VOLUNTEER = API_URL + "/registration/volunteer";
    public static final String REGISTER_ORGANIZATION = API_URL + "/registration/organization";
    public static final String UPDATE_VOLUNTEER_PROFILE = API_URL + "/update/volunteer";
    public static final String UPDATE_ORGANIZATION_PROFILE = API_URL + "/update/organization";
    /**
     * Organization routes
     */
    public static final String ORGANIZATION = API_URL + "/organization";
    public static final String GET_ALL_ORGANIZATIONS = ORGANIZATION + "/get_all";
    public static final String GET_ORGANIZATION_PROFILE = ORGANIZATION + "/my_profile";

    /**
     * Volunteers routes
     */
    public static final String VOLUNTEER = API_URL + "/volunteer";
    public static final String GET_VOLUNTEER_PROFILE = VOLUNTEER + "/my_profile";
    public static final String JOIN_TO_ACTIVITY = VOLUNTEER + "/enter_to_activity";
    public static final String SET_VOLUNTEERS_PREFERENCES = VOLUNTEER + "/set_preferences";
    public static final String LEAVE_FROM_ACTIVITY_BY_VOLUNTEER = VOLUNTEER + "/leave_activity";
    public static final String GET_RECOMMENDATION_BY_PREFERENCES = VOLUNTEER + "/get_recommendations";

    /**
     * Activity routes
     */
    public static final String ADD_ACTIVITY = ORGANIZATION + "/add_activity";
    public static final String GET_MY_ACTIVITIES = ORGANIZATION + "/my_activities";
    public static final String UPDATE_ACTIVITY_INFORMATION = ORGANIZATION + "/update_activity";
    public static final String ACTIVITY_CURRENT_ORGANIZATION = ORGANIZATION + "/get_activities";
    public static final String DELETE_CURRENT_ACTIVITY_BY_ID = ORGANIZATION + "/delete_activity";
    public static final String GET_ALL_ACTIVITIES_WITH_ALL_ORGANIZATIONS = ORGANIZATION + "/get_all_activities";

    /**
     * Feedback routes
     */
    public static final String FEEDBACK = API_URL + "/feedback";
    public static final String POST_FEEDBACK_ABOUT_ORGANIZATION = FEEDBACK + "/give_feedback";
    public static final String GET_FEEDBACKS_OF_ALL_ORGANIZATIONS = FEEDBACK + "/get_all";
    public static final String UPDATE_FEEDBACK_FOR_CURRENT_ORGANIZATION = FEEDBACK + "/update_feedback";
    public static final String GET_FEEDBACKS_FROM_CURRENT_ORGANIZATION = FEEDBACK + "/feedbacks_of_organization";
    public static final String DELETE_FEEDBACK_ABOUT_ORGANIZATION = FEEDBACK + "/remove_feedback";
    /**
     * Location routes
     */
    public static final String LOCATION = API_URL + "/location";
    public static final String FIND_NEARBY_ACTIVITIES = LOCATION + "/find_nearby";
    /**
     * Notifications routes
     */
    public static final String NOTIFICATIONS = API_URL + "/notifications";
    public static final String GET_ALL_SUBSCRIPTIONS_OF_VOLUNTEER = NOTIFICATIONS + "/my_subscriptions";
    public static final String SUBSCRIBE_TO_NOTIFICATIONS_BY_ID_OF_ORGANIZATION = NOTIFICATIONS + "/subscribe";
    public static final String UNSUBSCRIBE_FROM_NOTIFICATIONS_BY_ID_OF_ORGANIZATION = NOTIFICATIONS + "/unsubscribe";
    /**
     * Social networks integration routes
     */
    public static final String SOCIAL_NETWORKS = API_URL + "/social";
    public static final String ADD_COMMUNITY_LINK = SOCIAL_NETWORKS + "/add_community_link";
    public static final String GET_CHAT_LINK_BY_ACTIVITY = SOCIAL_NETWORKS + "/get_chat_link";
    public static final String ADD_CHAT_LINK_FOR_ACTIVITY = SOCIAL_NETWORKS + "/add_chat_link";
    public static final String GET_COMMUNITY_LINK_BY_ORGANIZATION = SOCIAL_NETWORKS + "/get_community_link";

    public static final String[] SWAGGER_ENDPOINTS = {
            "api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
