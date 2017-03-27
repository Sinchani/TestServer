package net.media.spamserver;

import net.media.spamserver.config.DetectionConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.util.BasicUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Random;

/**
 * Created by vivek on 10/17/14.
 */

public class VisitorIdAdVerifierTest extends VerifierTest {

    private String sysGenvistorId = "37799166vr21089";
    private ClickDetails sysGenVisitorIdclick = new ClickDetails(IP, BasicUtil.ipToLong(IP), UANormal, BasicUtil.hash(UANormal), advertiserUrl, customerId, pubUrl,
            pubDomain, timeTaken, "000000" + Long.toString(new Date().getTime()) + "123132132", sysGenvistorId);

    private String getRandomIp() {
        Random r = new Random();
        return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
    }

    private int visitorIdVerifier(ClickDetails click){
        int result = 0,
                maxAllowed = DetectionConfig.MAX_VID_AD,
                sleepTime = (int)(((DetectionConfig.EXPIRE_VID_AD) / (DetectionConfig.MAX_VID_AD + 1)) * 0.7);
        for(int i = 0; i < maxAllowed + 1; i++) {
            result = requestHandlerService.getClickScore(click);
            click.setIpStr(getRandomIp());
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.out.println("-------------------------------------------------------------------------------------------");
        }
        return result;
    }

    @Test
    public void sysGenVistorIdAdVerifierTest(){
        sysGenVisitorIdclick.setPartnerId(partnerId);
        int result = visitorIdVerifier(sysGenVisitorIdclick);
        Assert.assertTrue(result == ReturnStatus.LEGITIMATE_CLICK);
    }

    @Test
    public void normalVisitorIdAdVerifierTest() {
        sysGenVisitorIdclick.setPartnerId(partnerId);
        int result = visitorIdVerifier(normalClick);
        Assert.assertTrue(result == ReturnStatus.VID_AD_SPAM);
    }
}
