package net.media.spamserver.verifiers;

import net.media.spamserver.config.DetectionConfig;
import net.media.spamserver.config.RedisConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.util.BasicUtil;
import org.springframework.stereotype.Component;

@Component
public class DomainIPVerifier extends Verifier {

    private StringBuilder getKey(ClickDetails click){
        StringBuilder key = new StringBuilder();
        key.append(RedisConfig.KEY_DOMAIN_IP);
        key.append(RedisConfig.SEP_KEY_TOKENS);
        key.append(click.getPublisherDomain());
        key.append(RedisConfig.SEP_KEY_TOKENS);
        key.append(click.getIpLong());
        key.append(RedisConfig.SEP_KEY_TOKENS);
        return key;
    }

    private int getCount(StringBuilder key) {
        return cacheDAL.incrementKey(this.jedis, key.toString(), getExpiry());
    }

    private int getExpiry() {
        return this.expiry == null ? DetectionConfig.EXPIRE_DOMAIN_IP : this.expiry;
    }

    private int getThreshold() {
        return this.expiry == null ? DetectionConfig.MAX_DOMAIN_IP : this.threshold;
    }

    @Override
    public int getScore(ClickDetails click) {
        if(! BasicUtil.isCorrectionAdIP(click.getIpStr())) {
            StringBuilder key = getKey(click);
            int count = getCount(key);
            if (count > getThreshold()) {
                return ReturnStatus.DOMAIN_IP_SPAM;
            }
        }
        return Verifier.defaultScore;
    }
}
