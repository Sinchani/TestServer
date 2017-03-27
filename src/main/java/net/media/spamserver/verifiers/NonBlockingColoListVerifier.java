package net.media.spamserver.verifiers;

import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.services.ColoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NonBlockingColoListVerifier extends Verifier{
    @Autowired private ColoListService coloListService;

    @Override
    public int getScore(ClickDetails click) {
        if (click.getInboundIP() != null) {
            boolean isColoIp = coloListService.isNonBlockingColoIp(click, this.jedis, click.getInboundIP());
            if (isColoIp) {
                return ReturnStatus.NON_BLOCKING_COLO_IP_SPAM;
            }
        }
        return Verifier.defaultScore;
    }
}