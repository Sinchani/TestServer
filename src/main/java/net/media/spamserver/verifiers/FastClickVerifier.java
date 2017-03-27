package net.media.spamserver.verifiers;

import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;

/**
 * Created by vivek on 8/25/15.
 */
public class FastClickVerifier extends Verifier {

    @Override
    public int getScore(ClickDetails click) {
        if (click.getTimeTaken() == 0) {
            return defaultScore;
        }
        if (click.getTimeTaken() < threshold) {
            return ReturnStatus.FAST_CLICK_SPAM;
        }
        return defaultScore;
    }
}