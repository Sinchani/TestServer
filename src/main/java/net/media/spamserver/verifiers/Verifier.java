package net.media.spamserver.verifiers;

import net.media.spamserver.config.VerifierConfig;
import net.media.spamserver.data.CacheDAL;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.services.ColoListService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public abstract class Verifier {
    protected Jedis jedis;
    public int priority;
    public static final int defaultScore = -1;
    protected Integer threshold;
    protected Integer expiry;
    @Autowired protected CacheDAL cacheDAL;
    @Autowired protected ColoListService coloListService;

    public void setCacheDAL(CacheDAL cacheDAL) {
        this.cacheDAL = cacheDAL;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public void setExpiry(Integer expiry) {
        this.expiry = expiry;
    }

    public Verifier() {
        this.priority = ArrayUtils.indexOf(VerifierConfig.priorities, this.getClass().getName());
    }

    public void setJedis(Jedis jedis){
        this.jedis = jedis;
    }

    public abstract int getScore(ClickDetails click);

    public void setColoListService(ColoListService coloListService) {
        this.coloListService = coloListService;
    }
}
