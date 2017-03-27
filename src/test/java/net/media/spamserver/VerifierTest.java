package net.media.spamserver;

import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.data.RedisRepository;
import net.media.spamserver.services.ColoListService;
import net.media.spamserver.services.RequestHandlerService;
import net.media.spamserver.util.BasicUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * Created by satheesh on 16/12/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class,
        classes={ApplicationConfig.class})
public abstract class VerifierTest {

    @Autowired protected RequestHandlerService requestHandlerService;
    @Autowired protected RedisRepository redisRepository;
    @Autowired protected ColoListService coloListService;
    protected String IP = "172.16.25.25";
    protected String UANormal = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1";
    protected String advertiserUrl = "http://advertiser.com";
    protected String normalVisitorId = "377991662108937";
    protected String pubUrl = "http://vivektest.com";
    protected String pubDomain = "vivektest.com";
    protected String customerId = "8CU2T3HV4";
    protected int timeTaken = 1;
    protected String partnerId = "par45";
    protected ClickDetails normalClick = new ClickDetails(IP, BasicUtil.ipToLong(IP), UANormal, BasicUtil.hash(UANormal), advertiserUrl, customerId, pubUrl,
            pubDomain, timeTaken, "000000" + Long.toString(new Date().getTime()) + "123132132", normalVisitorId);
    protected Jedis jedis;

    @Before
    public void setUp() {
        jedis = redisRepository.getRedisResource();
        emptyRedis();
        normalClick.setPartnerId(partnerId);
        /*coloListService.updateBlockingColoRange(clickDetails, !custom.equals("0"));
        coloListService.updateNonBlockingColoRange(clickDetails, custom.equals("0"));*/
    }

    @After
    public void tearDown() {
        redisRepository.releaseRedisResource(jedis);
        normalClick = new ClickDetails(IP, BasicUtil.ipToLong(IP), UANormal, BasicUtil.hash(UANormal), advertiserUrl, customerId,
                pubUrl, pubDomain, timeTaken, Long.toString(new Date().getTime() - 1356998400000L) + "123132132", normalVisitorId);
        normalClick.setPartnerId(partnerId);
    }

    protected void emptyRedis(){
        jedis.flushAll();
    }
}
