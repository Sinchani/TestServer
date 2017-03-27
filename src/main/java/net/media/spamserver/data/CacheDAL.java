package net.media.spamserver.data;

import net.media.spamserver.config.RedisConfig;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.model.ColoUpdateConfig;
import net.media.spamserver.services.ColoListService;
import net.media.spamserver.services.CustomKeyNamingService;
import net.media.spamserver.util.BasicUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

/**
 * Created by vivek on 3/17/15.
 */
@Repository
public class CacheDAL {
    @Autowired private RedisRepository redisRepository;
    @Autowired private Logger logger;
    @Autowired private CustomKeyNamingService customKeyNamingService;

    public int incrementKey(Jedis jedis, String key, int expire){
        try{
            int currKeyCount;
            int prevKeyCount = 0;
            long[] timeSuffixes = BasicUtil.getRedisSuffixes(expire);
            String currTimeSuffix = Long.toString(timeSuffixes[0]);
            String prevTimeSuffix = Long.toString(timeSuffixes[0] - 1);
            int currTimeSlot = (int) timeSuffixes[1];
            String redisCurrKey = key + RedisConfig.SEP_KEY_TIME_STAMP + currTimeSuffix;
            String redisPrevKey = key + RedisConfig.SEP_KEY_TIME_STAMP + prevTimeSuffix;
            String redisCurrValue = jedis.get(redisCurrKey);
            String[] currSlotValues = new String[0];
            if(redisCurrValue != null){
                currSlotValues = redisCurrValue.split(RedisConfig.SEP_VALUE_TIMESLOT);
            }
            if(currTimeSlot == 0) {
                //puts key 1 from no key
                if(currSlotValues.length == 0) {
                    redisCurrValue = "1";
                }
                //puts key 3 from key 2
                else {
                    redisCurrValue = String.valueOf(Integer.parseInt(currSlotValues[0]) + 1);
                }
            }
            else {
                //puts key 0@0@0@1 from no key
                if(currSlotValues.length == 0){
                    redisCurrValue = "0" + StringUtils.repeat(RedisConfig.SEP_VALUE_TIMESLOT + "0", currTimeSlot - 1);
                    redisCurrValue += RedisConfig.SEP_VALUE_TIMESLOT + "1";
                }
                //puts key 3@0@7@1 from 3@0@7
                else if(currTimeSlot == currSlotValues.length){
                    redisCurrValue += RedisConfig.SEP_VALUE_TIMESLOT + "1";
                }
                //puts key 3@0@7@3 from 3@0@7@2
                else if(currTimeSlot == currSlotValues.length - 1){
                    currSlotValues[currSlotValues.length - 1] = Integer.toString(Integer.parseInt(currSlotValues[currSlotValues.length - 1]) + 1);
                    redisCurrValue = StringUtils.join(currSlotValues, RedisConfig.SEP_VALUE_TIMESLOT);
                }
                //puts key 3@0@7@0@0@1 from 3@0@7
                else {
                    redisCurrValue += StringUtils.repeat(RedisConfig.SEP_VALUE_TIMESLOT + "0", currTimeSlot - currSlotValues.length - 1);
                    redisCurrValue += RedisConfig.SEP_VALUE_TIMESLOT + "1";
                }
            }
            jedis.set(redisCurrKey, redisCurrValue);
            jedis.expire(redisCurrKey, 2 * expire / 1000 + 1);
            currSlotValues = redisCurrValue.split(RedisConfig.SEP_VALUE_TIMESLOT);
            currKeyCount = this.getAggregatedValue(currSlotValues, RedisConfig.TIME_SLOTS_IN_VALUE);
            String redisPrevValue = jedis.get(redisPrevKey);
            if(redisPrevValue != null && currTimeSlot != RedisConfig.TIME_SLOTS_IN_VALUE - 1) {
                String[] prevSlotValues = redisPrevValue.split(RedisConfig.SEP_VALUE_TIMESLOT);
                prevKeyCount = this.getAggregatedValue(prevSlotValues, RedisConfig.TIME_SLOTS_IN_VALUE - currTimeSlot - 1);
            }
            return currKeyCount + prevKeyCount;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return 0;
        }
    }

    private int getStartValue(int lastSlotCount){
        if(lastSlotCount != -1) {
            return RedisConfig.TIME_SLOTS_IN_VALUE - lastSlotCount;
        }
        return 0;
    }

    private int getAggregatedValue(String[] slotValues, int lastSlotCount){
        int count = 0;
        int start;
        start = getStartValue(lastSlotCount);
        for(int i = start; i < slotValues.length; i++ ){
            try{
                count += Integer.parseInt(slotValues[i]);
            }
            catch (Exception e){
                logger.info("Can't get aggregated stat because: " + e.getMessage());
            }
        }
        return count;
    }

    public void updateRangeForColoList(ColoUpdateConfig coloUpdateConfig, final Object changeKeyMonitor) {
        try(Jedis jedis = redisRepository.getRedisResource()) {
            jedis.expire(coloUpdateConfig.getListKey(), 0);
            try {
                BufferedReader bufferedReader = new BufferedReader(coloUpdateConfig.getFileReader());
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] ips = line.split(",");
                    if (ips.length != 3) {
                        break;
                    }
                    Long[] longIPs = new Long[2];

                    longIPs[0] =  BasicUtil.ipToLong(ips[0].trim());
                    longIPs[1] =  BasicUtil.ipToLong(ips[1].trim());
                    long range = longIPs[1] - longIPs[0];
                    String member = longIPs[1].toString() + ":" + range + ":" + ips[2];

                    jedis.zadd("temp" + coloUpdateConfig.getListKey(), (double) longIPs[1], member);
                }
            } catch (IOException e) {
                return;
            }
            synchronized (changeKeyMonitor) {
                jedis.rename("temp" + coloUpdateConfig.getListKey(), coloUpdateConfig.getListKey());
            }
        }
    }

    public Set<String> getPossibleIPRanges(ClickDetails clickDetails, ColoUpdateConfig coloUpdateConfig, Jedis jedis, String ip) {
        Long longIp = BasicUtil.ipToLong(ip);
        String prefix = coloUpdateConfig.getListKey().contains(RedisConfig.NON_BLOCKING_COLO_KEY) ? RedisConfig.NON_BLOCKING_COLO_KEY : RedisConfig.BLOCKING_COLO_KEY;
        return redisRepository.multiZRangeByScore(jedis, longIp.toString(),
                customKeyNamingService.getCustomKey(clickDetails, CustomKeyNamingService.KeyLevel.CREATIVE, prefix),
                customKeyNamingService.getCustomKey(clickDetails, CustomKeyNamingService.KeyLevel.PORTFOLIO, prefix),
                customKeyNamingService.getCustomKey(clickDetails, CustomKeyNamingService.KeyLevel.CUSTOMER, prefix),
                customKeyNamingService.getCustomKey(clickDetails, CustomKeyNamingService.KeyLevel.PARTNER, prefix),
                prefix);
    }
}
