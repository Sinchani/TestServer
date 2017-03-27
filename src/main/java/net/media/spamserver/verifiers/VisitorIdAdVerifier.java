package net.media.spamserver.verifiers;

import net.media.spamserver.config.DetectionConfig;
import net.media.spamserver.config.RedisConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.util.BasicUtil;
import org.springframework.stereotype.Component;

@Component
public class VisitorIdAdVerifier extends Verifier {

    private int getCount(StringBuilder key){
        return cacheDAL.incrementKey(this.jedis, key.toString(), getExpiry());
    }

    private StringBuilder getKey(ClickDetails click){
        StringBuilder key = new StringBuilder();
        key.append(RedisConfig.KEY_VID_AD);
        key.append(RedisConfig.SEP_KEY_TOKENS);
        key.append(click.getVisitorID());
        key.append(RedisConfig.SEP_KEY_TOKENS);
        key.append(click.getAdvertiserUrl());
        return key;
    }

    private boolean isVisitorIdSet(ClickDetails click){
        return !(click == null || click.getVisitorID().equals(""));
    }

    private int getExpiry() {
        return this.expiry == null ? DetectionConfig.EXPIRE_VID_AD : this.expiry;
    }

    private int getThreshold() {
        return this.expiry == null ? DetectionConfig.MAX_VID_AD : this.threshold;
    }

    @Override
    public int getScore(ClickDetails click) {
        if(! isVisitorIdSet(click) || BasicUtil.isSystemGeneratedVisitorId(click.getVisitorID())){
            return Verifier.defaultScore;
        }
        StringBuilder key = getKey(click);
        int count = getCount(key);
        if(count > getThreshold()) {
            return ReturnStatus.VID_AD_SPAM;
        }
        return Verifier.defaultScore;
    }
}
