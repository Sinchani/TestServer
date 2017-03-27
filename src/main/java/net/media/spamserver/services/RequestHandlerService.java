package net.media.spamserver.services;

import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.model.Reply;
import net.media.spamserver.data.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RequestHandlerService {
    @Autowired private RedisRepository redisRepository;
    @Autowired private BotUaService botUaService;
    @Autowired private VerifierHandlingService verifierHandlingService;

    public Reply handleClick(ClickDetails clickDetails) {
        botUaService.initializeBotUaList();
        int clickScore = getClickScore(clickDetails);
        return new Reply(clickScore, Reply.ServerStatus.OK);
    }

    public int getClickScore(ClickDetails clickDetails) {
        return verifierHandlingService.getScore(clickDetails);
    }

    public void testRedis() {
        Jedis jedisTest = redisRepository.getRedisResource();
        redisRepository.releaseRedisResource(jedisTest);
    }
}
