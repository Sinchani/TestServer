package net.media.spamserver.verifiers;

import net.media.spamserver.config.DetectionConfig;
import net.media.spamserver.config.RedisConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.util.BasicUtil;
import org.springframework.stereotype.Component;

@Component
public class DomainVisitorIdVerifier extends Verifier {

    private String getKey(ClickDetails click){
        return RedisConfig.KEY_DOMAIN_VID +
                RedisConfig.SEP_KEY_TOKENS +
                click.getIpLong() +
                RedisConfig.SEP_KEY_TOKENS +
                click.getVisitorID();
    }

    private boolean isVisitorIdSet(ClickDetails click){
        return !(click == null || click.getVisitorID().equals(""));
    }

    private int getCount(String key){
        return cacheDAL.incrementKey(this.jedis, key, getExpiry());
    }

    private int getExpiry() {
        return this.expiry == null ? DetectionConfig.EXPIRE_DOMAIN_VID : this.expiry;
    }

    private int getThreshold() {
        return this.expiry == null ? DetectionConfig.MAX_DOMAIN_VID : this.threshold;
    }

    @Override
    public int getScore(ClickDetails click) {
        if(! isVisitorIdSet(click) || BasicUtil.isSystemGeneratedVisitorId(click.getVisitorID())){
            return Verifier.defaultScore;
        }
        String key = getKey(click);
        int count = getCount(key);
        if (count > getThreshold()) {
            return ReturnStatus.DOMAIN_VID_SPAM;
        }
        return Verifier.defaultScore;
    }
}