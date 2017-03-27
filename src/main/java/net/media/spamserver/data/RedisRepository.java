package net.media.spamserver.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RedisRepository {
    @Autowired @Qualifier("counterDataStorePool") private JedisPool jedisPool;

    public Jedis getRedisResource() {
        return jedisPool.getResource();
    }

    public void releaseRedisResource(Jedis jedis) {
        jedis.close();
    }

    public void set(String key, String value) {
        try(Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    public String getKey(String key) {
        try(Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public List<String> mGet(String... keys) {
        try(Jedis jedis = jedisPool.getResource()) {
            return jedis.mget(keys);
        }
    }

    public Set<String> multiZRangeByScore(Jedis jedis, String score, String... keys) {
        Pipeline pipeline = jedis.pipelined();
        List<Response<Set<String>>> responses = new ArrayList<>();
        Set<String> returnList = new LinkedHashSet<>();
        for (String key : keys) {
            responses.add(pipeline.zrangeByScore(key, score, "+inf", 0, 1));
        }
        pipeline.sync();
        for (Response<Set<String>> response : responses) {
            String [] result = new String[1];
            returnList.add(response.get().size() > 0 ? response.get().toArray(result)[0] : "");
        }
        return returnList;
    }

    public Set<String> multiZRangeByScore(String score, String... keys) {
        try(Jedis jedis = jedisPool.getResource()) {
            return multiZRangeByScore(jedis, score, keys);
        }
    }
}
