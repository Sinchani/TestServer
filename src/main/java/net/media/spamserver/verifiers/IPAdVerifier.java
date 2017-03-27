package net.media.spamserver.verifiers;

import net.media.spamserver.config.DetectionConfig;
import net.media.spamserver.config.RedisConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.util.BasicUtil;
import org.springframework.stereotype.Component;

@Component
public class IPAdVerifier extends Verifier {

    private StringBuilder getKey(ClickDetails click){
        StringBuilder key = new StringBuilder();
        key.append(RedisConfig.KEY_IP_AD);
        key.append(RedisConfig.SEP_KEY_TOKENS);
        key.append(click.getIpLong());
        key.append(RedisConfig.SEP_KEY_TOKENS);
        key.append(click.getAdvertiserUrl());
        return key;
    }

    private int getCount(StringBuilder key){
        return cacheDAL.incrementKey(this.jedis, key.toString(), getExpiry());
    }

    private int getExpiry() {
        return this.expiry == null ? DetectionConfig.EXPIRE_IP_AD : this.expiry;
    }

    private int getThreshold() {
        return this.expiry == null ? DetectionConfig.MAX_IP_AD : this.threshold;
    }

    @Override
    public int getScore(ClickDetails click) {
        if (! BasicUtil.isCorrectionAdIP(click.getIpStr())) {
            StringBuilder key = getKey(click);
            int count = getCount(key);
            if (count > getThreshold()) {
                return ReturnStatus.IP_AD_SPAM;
            }
        }
        return Verifier.defaultScore;
    }
}
