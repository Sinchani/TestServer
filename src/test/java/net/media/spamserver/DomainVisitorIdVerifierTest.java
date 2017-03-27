package net.media.spamserver;

import junit.framework.Assert;
import net.media.spamserver.config.DetectionConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.util.BasicUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Date;

public class DomainVisitorIdVerifierTest extends VerifierTest{

    private String sysGenvistorId = "37799166vr21089";
    private ClickDetails sysGenVisitorIdclick = new ClickDetails(IP, BasicUtil.ipToLong(IP), UANormal, BasicUtil.hash(UANormal), advertiserUrl, customerId, pubUrl,
            pubDomain, timeTaken, "000000" + Long.toString(new Date().getTime()) + "123132132", sysGenvistorId);

    private int domainVisitorIdVerifier(ClickDetails click) {
        emptyRedis();
        int result = 0;
        int maxAllowed = DetectionConfig.MAX_DOMAIN_VID;
        int sleepTime = (int)(((DetectionConfig.EXPIRE_DOMAIN_VID) / (DetectionConfig.MAX_DOMAIN_VID + 1)) * 0.7);
        for(int i = 0; i < maxAllowed + 1; i++) {
            click.setAdvertiserUrl(click.getAdvertiserUrl() + 'e');
            result = requestHandlerService.getClickScore(click);
            try {
                Thread.sleep(sleepTime);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.out.println("-------------------------------------------------------------------------------------------");
            click.setUserAgent(click.getUserAgent() + "Test");
        }
        return result;
    }

    @Test
    public void sysGenVisitorIdDomainVerifier(){
        sysGenVisitorIdclick.setPartnerId(partnerId);
        int result = domainVisitorIdVerifier(sysGenVisitorIdclick);
        Assert.assertTrue(result == ReturnStatus.LEGITIMATE_CLICK);
    }

    @Test
    public void normalVisitorIdDomainVerifier(){
        int result = domainVisitorIdVerifier(normalClick);
        Assert.assertTrue(result == ReturnStatus.DOMAIN_VID_SPAM);
    }
}
