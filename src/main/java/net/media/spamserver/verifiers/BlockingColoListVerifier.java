package net.media.spamserver.verifiers;

import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import org.springframework.stereotype.Component;

/**
 * Created by vivek on 4/20/15.
 */
@Component
public class BlockingColoListVerifier extends Verifier {
    @Override
    public int getScore(ClickDetails click) {
        if (click.getInboundIP() != null) {
            boolean isColoIp = coloListService.isBlockingColoIp(click, this.jedis, click.getInboundIP());
            if (isColoIp) {
                return ReturnStatus.BLOCKING_COLO_IP_SPAM;
            }
        }
        return Verifier.defaultScore;
    }
}