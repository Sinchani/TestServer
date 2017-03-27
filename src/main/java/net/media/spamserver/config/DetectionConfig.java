package net.media.spamserver.config;

import java.util.Properties;

public class DetectionConfig {
    private static final Properties properties = SysProperties.getInstance();
    public static final int EXPIRE_DOMAIN_IP = Integer.parseInt(properties.getProperty("EXPIRE_DOMAIN_IP"));
    public static final int EXPIRE_IP_AD = Integer.parseInt(properties.getProperty("EXPIRE_IP_AD"));
    public static final int EXPIRE_DOMAIN_VID = Integer.parseInt(properties.getProperty("EXPIRE_DOMAIN_VID"));
    public static final int EXPIRE_VID_AD = Integer.parseInt(properties.getProperty("EXPIRE_VID_AD"));
    public static final int EXPIRE_VISIT_ID = Integer.parseInt(properties.getProperty("EXPIRE_VISIT_ID"));
    public static final int EXPIRE_VISIT_ID_COUNT = Integer.parseInt(properties.getProperty("EXPIRE_VISIT_ID_COUNT"));
    public static final int MAX_DOMAIN_IP = Integer.parseInt(properties.getProperty("MAX_DOMAIN_IP"));
    public static final int MAX_IP_AD = Integer.parseInt(properties.getProperty("MAX_IP_AD"));
    public static final int MAX_DOMAIN_VID = Integer.parseInt(properties.getProperty("MAX_DOMAIN_VID"));
    public static final int MAX_VID_AD = Integer.parseInt(properties.getProperty("MAX_VID_AD"));
    public static final int MAX_VISIT_ID_DURATION = Integer.parseInt(properties.getProperty("MAX_VISIT_ID_DURATION"));
    public static final int MAX_VISIT_ID_COUNT = Integer.parseInt(properties.getProperty("MAX_VISIT_ID_COUNT"));
}
