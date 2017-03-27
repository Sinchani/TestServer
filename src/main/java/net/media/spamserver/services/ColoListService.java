package net.media.spamserver.services;

import net.media.spamserver.config.RedisConfig;
import net.media.spamserver.data.CacheDAL;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.model.ColoUpdateConfig;
import net.media.spamserver.util.BasicUtil;
import net.media.spamserver.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by vivek on 2/18/15.
 */
@Service
public class ColoListService {
    @Autowired private CacheDAL cacheDAL;
    @Autowired private CustomKeyNamingService customKeyNamingService;
    private final Object changeKeyMonitor = new Object();

    public void updateNonBlockingColoRange(String entityId) {
        entityId = entityId.equals("0") ? "" : entityId;
        ColoUpdateConfig coloUpdateConfig = new ColoUpdateConfig(FileUtil.getNonBlockingColoRangeFileReader(entityId),
                customKeyNamingService.getCustomKeyFromEntityId(entityId, RedisConfig.NON_BLOCKING_COLO_KEY));
        cacheDAL.updateRangeForColoList(coloUpdateConfig, changeKeyMonitor);
    }

    public void updateBlockingColoRange(String entityId) {
        entityId = entityId.equals("0") ? "" : entityId;
        ColoUpdateConfig coloUpdateConfig = new ColoUpdateConfig(FileUtil.getBlockingColoRangeFileReader(entityId),
                customKeyNamingService.getCustomKeyFromEntityId(entityId, RedisConfig.BLOCKING_COLO_KEY));
        cacheDAL.updateRangeForColoList(coloUpdateConfig, changeKeyMonitor);
    }

    public boolean isBlockingColoIp(ClickDetails clickDetails, Jedis jedis, String ip) {
        synchronized (changeKeyMonitor) {
            ColoUpdateConfig coloUpdateConfig =
                    new ColoUpdateConfig(null, RedisConfig.BLOCKING_COLO_KEY);
            return isIpInRange(clickDetails, coloUpdateConfig, jedis, ip);
        }
    }

    public boolean isNonBlockingColoIp(ClickDetails clickDetails, Jedis jedis, String ip) {
        synchronized (changeKeyMonitor) {
            ColoUpdateConfig coloUpdateConfig = new ColoUpdateConfig(null, RedisConfig.NON_BLOCKING_COLO_KEY);
            return isIpInRange(clickDetails, coloUpdateConfig, jedis, ip);
        }
    }

    private boolean isIpInRange(ClickDetails clickDetails, ColoUpdateConfig coloUpdateConfig, Jedis jedis, String ip) {
        Long longIp = BasicUtil.ipToLong(ip);
        System.out.println("******  : " + longIp);
        Set<String> result = cacheDAL.getPossibleIPRanges(clickDetails, coloUpdateConfig, jedis, ip);

        for (String str : result) {
            if(!str.contains(":")) {
                continue;
            }
            String[] splitString = str.split(":");
            Long ipMax = Long.parseLong(splitString[0]);
            Long range = Long.parseLong(splitString[1]);

            if (longIp >= ipMax - range) {
                return true;
            }
        }
        return false;
    }

    private String getColoListKey(ClickDetails clickDetails, boolean blocking, boolean customList, CustomKeyNamingService.KeyLevel level) {
        if(customList) {
            if(blocking) {
                return customKeyNamingService.getCustomKey(clickDetails, level, RedisConfig.BLOCKING_COLO_KEY);
            } else {
                return customKeyNamingService.getCustomKey(clickDetails, level, RedisConfig.NON_BLOCKING_COLO_KEY);
            }
        } else {
            if(blocking) {
                return RedisConfig.BLOCKING_COLO_KEY;
            } else {
                return RedisConfig.NON_BLOCKING_COLO_KEY;
            }
        }
    }
}