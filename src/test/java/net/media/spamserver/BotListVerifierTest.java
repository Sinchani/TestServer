package net.media.spamserver;

import junit.framework.Assert;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.services.BotUaService;
import net.media.spamserver.util.BasicUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by vivek on 10/17/14.
 */

public class BotListVerifierTest extends VerifierTest{

    @Autowired
    private BotUaService botUaService;
    private String UABot = "stuff stuff Sogou web spider/4.0";
    private ClickDetails botSpamClick = new ClickDetails(IP, BasicUtil.ipToLong(IP), UABot, BasicUtil.hash(UABot), advertiserUrl, customerId,
            pubUrl, pubDomain, timeTaken, "000000" + Long.toString(new Date().getTime()) + "123132132", normalVisitorId);

    public void updateBotList(){
        botUaService.updateBotList();
    }

    @Test
    public void BotClickShouldBeSpam(){
        updateBotList();
        botSpamClick.setPartnerId(partnerId);
        int result = requestHandlerService.handleClick(botSpamClick).getSpamResult();
        Assert.assertTrue(result == ReturnStatus.BOT_UA_SPAM);
    }

    @Test
    public void NormalClickShouldNotBeSpam(){
        int result = requestHandlerService.handleClick(normalClick).getSpamResult();
        Assert.assertTrue(result == ReturnStatus.LEGITIMATE_CLICK);
    }
}
