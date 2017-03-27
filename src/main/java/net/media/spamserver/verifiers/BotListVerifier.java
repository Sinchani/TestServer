package net.media.spamserver.verifiers;

import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.services.BotUaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class BotListVerifier extends Verifier{
    @Autowired private BotUaService botUaService;

    private boolean isBot(ClickDetails click){
        return botUaService.isBotUA(click.getUserAgent());
    }

    @Override
    public synchronized int getScore(ClickDetails click) {
        if(isBot(click)) {
            return ReturnStatus.BOT_UA_SPAM;
        }
        return Verifier.defaultScore;
    }
}
