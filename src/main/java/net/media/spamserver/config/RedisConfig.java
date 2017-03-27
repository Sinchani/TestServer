package net.media.spamserver.config;

import java.util.Properties;

public class RedisConfig {
    private static Properties properties = SysProperties.getInstance();
    public static String SERVER_IP = properties.getProperty("SPAM_REDIS_SERVER");
    public static int SERVER_PORT = Integer.parseInt(properties.getProperty("SPAM_REDIS_PORT"));
    public static String SETTINGS_REDIS_IP = properties.getProperty("SETTINGS_REDIS_IP");
    public static int SETTINGS_REDIS_PORT = Integer.parseInt(properties.getProperty("SETTINGS_REDIS_PORT"));
    public static String KEY_DOMAIN_IP = properties.getProperty("KEY_DOMAIN_IP");
    public static String KEY_IP_AD = properties.getProperty("KEY_IP_AD");
    public static String KEY_DOMAIN_VID = properties.getProperty("KEY_DOMAIN_VID");
    public static String KEY_VID_AD = properties.getProperty("KEY_VID_AD");
    public static String KEY_VISIT_ID_COUNT = properties.getProperty("KEY_VISIT_ID_COUNT");
    public static String KEY_REG_VISIT_ID_COUNT = properties.getProperty("KEY_REG_VISIT_ID_COUNT");
    public static String KEY_CUSTOM_VERIFIER_SETTING = properties.getProperty("KEY_CUSTOM_VERIFIER_SETTING");
    public static String KEY_CUSTOM_VERIFIER = properties.getProperty("KEY_CUSTOM_VERIFIER");
    public static String SEP_KEY_TIME_STAMP = properties.getProperty("SEP_KEY_TIME_STAMP");
    public static String SEP_KEY_TOKENS = properties.getProperty("SEP_KEY_TOKENS");
    public static String SEP_VALUE_TIMESLOT = properties.getProperty("SEP_VALUE_TIMESLOT");
    public static String NON_BLOCKING_COLO_KEY = properties.getProperty("NON_BLOCKING_COLO_KEY");
    public static String BLOCKING_COLO_KEY = properties.getProperty("BLOCKING_COLO_KEY");
    public static String STRICTLY_BLOCKING_COLO_KEY = properties.getProperty("STRICTLY_BLOCKING_COLO_KEY");
    public static int TIME_SLOTS_IN_VALUE = Integer.parseInt(properties.getProperty("TIME_SLOTS_IN_VALUE"));

    public static int REDIS_MAX_CONNECTIONS = Integer.parseInt(properties.getProperty("REDIS_MAX_CONNECTIONS"));
    public static int REDIS_MAX_IDLE_CONNECTIONS = Integer.parseInt(properties.getProperty("REDIS_MAX_IDLE_CONNECTIONS"));
    public static int REDIS_MIN_IDLE_CONNECTIONS = Integer.parseInt(properties.getProperty("REDIS_MIN_IDLE_CONNECTIONS"));
    public static int REDIS_MAX_WAIT_MILLIS = Integer.parseInt(properties.getProperty("REDIS_MAX_WAIT_MILLIS"));
    public static boolean REDIS_TEST_ON_BORROW = Boolean.parseBoolean(properties.getProperty("REDIS_TEST_ON_BORROW"));
    public static int REDIS_MIN_EVICTABLE_TIME_MILLIS = Integer.parseInt(properties.getProperty("REDIS_MIN_EVICTABLE_TIME_MILLIS"));
    public static int REDIS_SOFT_MIN_EVICTABLE_TIME_MILLIS = Integer.parseInt(properties.getProperty("REDIS_SOFT_MIN_EVICTABLE_TIME_MILLIS"));
    public static String CUR_COLO_NON_BLOCKING_KEY = properties.getProperty("CUR_COLO_NON_BLOCKING_KEY");
    public static String CUR_COLO_BLOCKING_KEY = properties.getProperty("CUR_COLO_BLOCKING_KEY");
    public static String CUR_COLO_STRICTLY_BLOCKING_KEY = properties.getProperty("CUR_COLO_STRICTLY_BLOCKING_KEY");
}
