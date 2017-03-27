package net.media.spamserver.verifiers;

import net.media.spamserver.config.RedisConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.data.CacheDAL;
import net.media.spamserver.model.ClickDetails;

import java.util.List;

/**
 * Created by vivek on 3/24/15.
 */
public class CustomVerifier extends Verifier {
    private int returnStatus;
    private int threshold;
    private int expiry;
    private CacheDAL cacheDAL;
    private List<String> values;
    private List<String> keys;
    private String keyPrefix;

    public CustomVerifier(List<String> keys, List<String> values, int threshold, int expiry, int returnStatus) {
        this.keys = keys;
        this.values = values;
        this.threshold = threshold;
        this.expiry = expiry;
        this.returnStatus = returnStatus;
    }

    public void setReturnStatus(int returnStatus) {
        this.returnStatus = returnStatus;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public void setCacheDAL(CacheDAL cacheDAL) {
        this.cacheDAL = cacheDAL;
    }

    public CustomVerifier() {}

    private String getKey() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RedisConfig.KEY_CUSTOM_VERIFIER);
        stringBuilder.append(RedisConfig.SEP_KEY_TOKENS);
        stringBuilder.append(keyPrefix);
        stringBuilder.append(RedisConfig.SEP_KEY_TOKENS);
        for (int i = 0; i < keys.size(); i++) {
            stringBuilder.append(keys.get(i));
            stringBuilder.append(RedisConfig.SEP_KEY_TOKENS);
            stringBuilder.append(values.get(i));
        }
        return stringBuilder.toString();
    }

    @Override
    public int getScore(ClickDetails click) {
        if (keys == null) {
            return defaultScore;
        }
        String key = getKey();
        if (cacheDAL.incrementKey(jedis, key, expiry) > threshold) {
            return returnStatus == 0 ? ReturnStatus.DEFAULT_CUSTOM_VERIFIER_SPAM : returnStatus;
        }
        return defaultScore;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }
}
