package net.media.spamserver.config;

import java.util.Properties;

public class Config {
    private static final Properties properties = SysProperties.getInstance();
    public static final String BOT_LIST_LOCATION = properties.getProperty("BOT_LIST_LOCATION");
    public static final String BOT_SPIDERS_LIST_LOCATION = properties.getProperty("BOT_SPIDERS_LIST_LOCATION");
    public static final String COLO_NON_BLOCKING_FILE_PREFIX = properties.getProperty("COLO_NON_BLOCKING_FILE_PREFIX");
    public static final String COLO_BLOCKING_FILE_PREFIX = properties.getProperty("COLO_BLOCKING_FILE_PREFIX");
    public static final String COLO_LIST_LOCATION = properties.getProperty("COLO_LIST_LOCATION");
}
