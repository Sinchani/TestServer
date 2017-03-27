package net.media.spamserver;

import net.media.spamserver.config.DetectionConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.util.BasicUtil;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * Created by vivek on 10/17/14.
 */

public class DomainIpVerifierTest extends VerifierTest {

    private String correctionAdIP = "222.222.222.12";
    private ClickDetails correctionAdIPClick = new ClickDetails(correctionAdIP, BasicUtil.ipToLong(correctionAdIP), UANormal, BasicUtil.hash(UANormal), advertiserUrl, customerId,
           pubUrl, pubDomain, timeTaken, "000000" + Long.toString(new Date().getTime()) + "123132132", normalVisitorId);

    private int DomainIPVerifier(ClickDetails click) {
        emptyRedis();
        int result = 0,
                maxAllowed = DetectionConfig.MAX_DOMAIN_IP,
                sleepTime = (int) (((DetectionConfig.EXPIRE_DOMAIN_IP) / (DetectionConfig.MAX_DOMAIN_IP + 1)) * 0.7);
        for (int i = 0; i < maxAllowed + 1; i++) {
            click.setAdvertiserUrl(click.getAdvertiserUrl() + 'e');
            result = requestHandlerService.getClickScore(click);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.out.println("-------------------------------------------------------------------------------------------");
            click.setUserAgent(click.getUserAgent() + "Test");
            click.setVisitorID(click.getVisitorID() + "1");
        }
        return result;
    }

    @Test
    public void normalIPDomainSpamCheck() {
        int result = DomainIPVerifier(normalClick);
        Assert.assertTrue(result == ReturnStatus.DOMAIN_IP_SPAM);
    }

    @Test
    public void correctionAdIPDomainSpamCheck(){
        correctionAdIPClick.setPartnerId(partnerId);
        int result = DomainIPVerifier(correctionAdIPClick);
        Assert.assertTrue(result == ReturnStatus.LEGITIMATE_CLICK);
    }
}
