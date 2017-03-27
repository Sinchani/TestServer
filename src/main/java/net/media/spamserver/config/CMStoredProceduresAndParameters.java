package net.media.spamserver.config;

/**
 * Created by vivek on 5/27/15.
 */
public interface CMStoredProceduresAndParameters {
    public static final String CM_GET_CUSTOMER_DETAILS = "CM_Get_Customer_Details";
    public static final String CM_GET_PID_DETAILS = "GET_REAL_PID_DETAILS";
    public static final String CM_GET_ADTAG_DETAILS = "CM_Get_Customer_Ad_Unit_by_creative_key";
    public static final String CM_GET_ENTITY_ATTRIBUTE_DETAILS = "CM_Get_Entity_Attribute_Details";
    public static final String PARAM_ENTITY_ID = "entity_id";
    public static final String PARAM_SEARCH_BY_CUSTOMER = "search_by_customer";
    public static final String PARAM_CUSTOMER_ID = "customer_id";
    public static final String PARAM_PORTFOLIO_ID = "portfolio_id";
    public static final String PARAM_CREATIVE_ID = "creative_key";
    public static final String PARAM_SIZE = "size";
}