package net.media.spamserver.config;

import java.util.Properties;

public class ReturnStatus {
    private static final Properties properties = SysProperties.getInstance();
    public static final int LEGITIMATE_CLICK = Integer.parseInt(properties.getProperty("LEGITIMATE_CLICK"));
    public static final int BOT_UA_SPAM = Integer.parseInt(properties.getProperty("BOT_UA_SPAM"));
    public static final int VISIT_ID_EXPIRED = Integer.parseInt(properties.getProperty("VISIT_ID_EXPIRED"));
    public static final int DOMAIN_IP_SPAM = Integer.parseInt(properties.getProperty("DOMAIN_IP_SPAM"));
    public static final int IP_AD_SPAM = Integer.parseInt(properties.getProperty("IP_AD_SPAM"));
    public static final int VID_AD_SPAM = Integer.parseInt(properties.getProperty("VID_AD_SPAM"));
    public static final int DOMAIN_VID_SPAM = Integer.parseInt(properties.getProperty("DOMAIN_VID_SPAM"));
    public static final int NON_BLOCKING_COLO_IP_SPAM = Integer.parseInt(properties.getProperty("NON_BLOCKING_COLO_IP_SPAM"));
    public static final int BLOCKING_COLO_IP_SPAM = Integer.parseInt(properties.getProperty("BLOCKING_COLO_IP_SPAM"));
    public static final int STRICTLY_BLOCKING_COLO_IP_SPAM = Integer.parseInt(properties.getProperty("STRICTLY_BLOCKING_COLO_IP_SPAM"));
    public static final int DEFAULT_CUSTOM_VERIFIER_SPAM = Integer.parseInt(properties.getProperty("DEFAULT_CUSTOM_VERIFIER_SPAM"));
    public static final int FAST_CLICK_SPAM = Integer.parseInt(properties.getProperty("FAST_CLICK_SPAM"));
}
