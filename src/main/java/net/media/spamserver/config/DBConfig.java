package net.media.spamserver.config;

import java.util.Properties;

/**
 * Created by vivek on 3/17/15.
 */
public class DBConfig {
    private static final Properties properties = SysProperties.getInstance();
    public static final String DB_USERNAME = properties.getProperty("DB_USERNAME");
    public static final String DB_PASSWD = properties.getProperty("DB_PASSWD");
    public static final String DB_DRIVER = properties.getProperty("DB_DRIVER");
    public static final String DB_JDBC_URL = properties.getProperty("DB_JDBC_URL");
    public static final int DB_MIN_POOL_SIZE = Integer.parseInt(properties.getProperty("DB_MIN_POOL_SIZE"));
    public static final int DB_MAX_POOL_SIZE = Integer.parseInt(properties.getProperty("DB_MAX_POOL_SIZE"));
    public static final int DB_INIT_POOL_SIZE = Integer.parseInt(properties.getProperty("DB_INIT_POOL_SIZE"));
    public static final int DB_MAX_STATEMENTS = Integer.parseInt(properties.getProperty("DB_MAX_STATEMENTS"));
    
}
