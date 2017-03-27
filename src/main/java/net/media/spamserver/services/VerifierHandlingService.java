package net.media.spamserver.services;

import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.data.CacheDAL;
import net.media.spamserver.data.RedisRepository;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.verifiers.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class VerifierHandlingService {
    @Autowired private EntityService entityService;
    @Autowired private List<Verifier> defaultVerifiers;
    @Autowired private RedisRepository redisRepository;

    private synchronized void sortVerifiers() {
        Collections.sort(this.defaultVerifiers, new Comparator<Verifier>() {
            public int compare(Verifier o1, Verifier o2) {
                return o1.priority - o2.priority;
            }
        });
    }

    public int getScore(ClickDetails click) {
        try(Jedis jedis = redisRepository.getRedisResource()) {
            List<Verifier> customVerifierList = entityService.getVerifierList(click);
            if (customVerifierList == null) {
                sortVerifiers();
                return getStatusUsingDefaultVerifiers(click, jedis);
            }
            return getStatusUsingCustomVerifiers(customVerifierList, click, jedis);
        }
    }

    private int getStatusUsingCustomVerifiers(List<Verifier> customVerifierList, ClickDetails click, Jedis jedis) {
        return getStatusFromVerifierList(customVerifierList, click, jedis);
    }

    private int getStatusUsingDefaultVerifiers(ClickDetails click, Jedis jedis) {
        return getStatusFromVerifierList(this.defaultVerifiers, click, jedis);
    }

    private int getStatusFromVerifierList(List<Verifier> verifiers, ClickDetails clickDetails, Jedis jedis) {
        for (Verifier verifier : verifiers) {
            verifier.setJedis(jedis);
            int score = verifier.getScore(clickDetails);
            if (score > 0) {
                return score;
            }
        }
        return ReturnStatus.LEGITIMATE_CLICK;
    }
}
