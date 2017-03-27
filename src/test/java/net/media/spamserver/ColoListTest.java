package net.media.spamserver;

import junit.framework.Assert;
import net.media.spamserver.config.RedisConfig;
import net.media.spamserver.config.ReturnStatus;
import net.media.spamserver.data.RedisRepository;
import net.media.spamserver.model.ColoUpdateConfig;
import net.media.spamserver.services.ColoListService;
import net.media.spamserver.util.FileUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/**
 * Created by vivek on 2/18/15.
 */
public class ColoListTest extends VerifierTest {

    @Test
    public void IsBlockingColoIp() {
        normalClick.setIpStr("66.249.83.177");
        int result = requestHandlerService.getClickScore(normalClick);
        Assert.assertTrue(result == ReturnStatus.NON_BLOCKING_COLO_IP_SPAM);
    }

    @Test
    public void IsNonBlockingColoIp() {
        normalClick.setIpStr("216.185.63.21");
        int result = requestHandlerService.getClickScore(normalClick);
        Assert.assertTrue(result == ReturnStatus.NON_BLOCKING_COLO_IP_SPAM);
    }

    @Test
    public void IsNotNonBlockingColoIp() {
        normalClick.setIpStr("127.0.0.1");
        int result = requestHandlerService.getClickScore(normalClick);
        Assert.assertTrue(result != ReturnStatus.NON_BLOCKING_COLO_IP_SPAM);
    }

    @Test
    public void IsNotBlockingColoIp() {
        normalClick.setIpStr("216.185.126.22");
        int result = requestHandlerService.getClickScore(normalClick);
        Assert.assertTrue(result != ReturnStatus.NON_BLOCKING_COLO_IP_SPAM);
    }
}
