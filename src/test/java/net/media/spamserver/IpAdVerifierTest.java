
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

public class IpAdVerifierTest extends VerifierTest{

    private String correctionAdIp = "222.222.222.12";
    private ClickDetails correctionAdIpClick = new ClickDetails(correctionAdIp, BasicUtil.ipToLong(correctionAdIp), UANormal, BasicUtil.hash(UANormal), advertiserUrl, customerId, pubUrl,
            pubDomain, timeTaken, "000000" + Long.toString(new Date().getTime()) + "123132132", normalVisitorId);

    private int IPAdVerifier(ClickDetails click) {
        int result = 0,
                maxAllowed = DetectionConfig.MAX_IP_AD,
                sleepTime = (int)(((DetectionConfig.EXPIRE_IP_AD) / (DetectionConfig.MAX_IP_AD + 1)) * 0.7);
        for(int i=0; i < maxAllowed + 1; i++)
        {
            result = requestHandlerService.getClickScore(click);
            try {
                Thread.sleep(sleepTime);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.out.println("-------------------------------------------------------------------------------------------");
            click.setVisitorID(click.getVisitorID()+ '1');
        }
        return result;
    }

    @Test
    public void normalIPAdSpamCheck(){
        int result = IPAdVerifier(normalClick);
        Assert.assertTrue(result == ReturnStatus.IP_AD_SPAM);
    }
    @Test
    public void correctionAdIPAdSpamCheck(){
        correctionAdIpClick.setPartnerId(partnerId);
        int result = IPAdVerifier(correctionAdIpClick);
        Assert.assertTrue(result == ReturnStatus.LEGITIMATE_CLICK);
    }
}

