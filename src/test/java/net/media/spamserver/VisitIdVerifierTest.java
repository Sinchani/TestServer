package net.media.spamserver;

import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.util.BasicUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by vivek on 10/17/14.
 */

public class VisitIdVerifierTest extends VerifierTest {

    private ClickDetails visitIdSpamClick = new ClickDetails(IP, BasicUtil.ipToLong(IP), UANormal, BasicUtil.hash(UANormal), advertiserUrl, customerId,
            pubUrl, pubDomain, timeTaken, "0000001356998400123132132", normalVisitorId);

    @Test
    public void VisitIdCheckForNormalClick() {
        int result = requestHandlerService.getClickScore(normalClick);
        Assert.assertFalse(result == ReturnStatus.VISIT_ID_EXPIRED);
    }

    @Test
    public void VisitIdCheckForSpamClick() {
        visitIdSpamClick.setPartnerId(partnerId);
        int result = requestHandlerService.getClickScore(visitIdSpamClick);
        Assert.assertTrue(result == ReturnStatus.VISIT_ID_EXPIRED);
    }
}

