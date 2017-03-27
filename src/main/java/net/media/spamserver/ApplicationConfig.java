package net.media.spamserver;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.media.spamserver.config.DBConfig;
import net.media.spamserver.config.RedisConfig;
import org.ahocorasick.trie.Trie;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@ComponentScan(basePackages = "net.media.spamserver")
public class ApplicationConfig {

    private GenericObjectPoolConfig getConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(RedisConfig.REDIS_MAX_CONNECTIONS);
        genericObjectPoolConfig.setMaxIdle(RedisConfig.REDIS_MAX_IDLE_CONNECTIONS);
        genericObjectPoolConfig.setMinIdle(RedisConfig.REDIS_MIN_IDLE_CONNECTIONS);
        genericObjectPoolConfig.setMaxWaitMillis(RedisConfig.REDIS_MAX_WAIT_MILLIS);
        genericObjectPoolConfig.setTestOnBorrow(RedisConfig.REDIS_TEST_ON_BORROW);
        genericObjectPoolConfig.setMinEvictableIdleTimeMillis(RedisConfig.REDIS_MIN_EVICTABLE_TIME_MILLIS);
        genericObjectPoolConfig.setSoftMinEvictableIdleTimeMillis(RedisConfig.REDIS_SOFT_MIN_EVICTABLE_TIME_MILLIS);
        return genericObjectPoolConfig;
    }

    @Bean
    public Logger logger() {
        BasicConfigurator.configure();
        return Logger.getRootLogger();
    }

    @Bean(name = "counterDataStorePool")
    public JedisPool jedisPool() {
        return new JedisPool(getConfig(),
                RedisConfig.SERVER_IP, RedisConfig.SERVER_PORT);
    }

    @Bean
    public Trie ahoCorasick() {
        return new Trie();
    }

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(DBConfig.DB_USERNAME);
        dataSource.setPassword(DBConfig.DB_PASSWD);
        dataSource.setDriverClass(DBConfig.DB_DRIVER);
        dataSource.setJdbcUrl(DBConfig.DB_JDBC_URL);
        dataSource.setMinPoolSize(DBConfig.DB_MIN_POOL_SIZE);
        dataSource.setMaxPoolSize(DBConfig.DB_MAX_POOL_SIZE);
        dataSource.setInitialPoolSize(DBConfig.DB_INIT_POOL_SIZE);
        dataSource.setMaxStatements(DBConfig.DB_MAX_STATEMENTS);
        return dataSource;
    }

    @Bean(name = "settingsDataStorePool")
    public JedisPool getSettingsDataPool() {
        return new JedisPool(getConfig(), RedisConfig.SETTINGS_REDIS_IP, RedisConfig.SETTINGS_REDIS_PORT);
    }
}
