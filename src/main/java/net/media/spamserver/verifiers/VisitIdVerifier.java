package net.media.spamserver.verifiers;

import net.media.spamserver.config.DetectionConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class VisitIdVerifier extends Verifier{

    private boolean isVisitIdExpired(long visitIdTime){
        return new Date().getTime() - visitIdTime > getThreshold();
    }

    private boolean isVisitIdSet(String visitId){
        return ! (visitId == null || visitId.equals(""));
    }

    private long getGeneratedTime(String visitId) {
        if(visitId == null || visitId.length() < 20){
            return 0;
        }
        String temp = visitId.replaceFirst("^1+(?!$)", "");
        temp = temp.replaceFirst("^0+(?!$)", "");
        String time = temp.substring(0, Math.min(temp.length(), 13));
        return Long.parseLong(time);
    }

    private int getThreshold() {
        return this.threshold == null ? DetectionConfig.MAX_VISIT_ID_DURATION : this.threshold;
    }

    @Override
    public int getScore(ClickDetails click) {
        String visitId = click.getVisitID();
        if(!isVisitIdSet(visitId) || isBannedForCustomer(click.getCustomerId())
                || !isNumberString(visitId) || visitId.length() != 32) {
            return Verifier.defaultScore;
        }
        long visitIdTime = getGeneratedTime(visitId);
        if(isVisitIdExpired(visitIdTime)) {
            return ReturnStatus.VISIT_ID_EXPIRED;
        }
        return Verifier.defaultScore;
    }

    private boolean isNumberString(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isDigit(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isBannedForCustomer(String customerId) {
        List<String> excludedCustomers = jedis.lrange("VisitIdBannedCustomers", 0, -1);
        return excludedCustomers.contains(customerId);
    }
}
