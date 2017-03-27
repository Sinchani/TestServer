package net.media.spamserver.controllers;

import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.model.ClickDetailsBuilder;
import net.media.spamserver.model.Reply;
import net.media.spamserver.services.BotUaService;
import net.media.spamserver.services.ColoListService;
import net.media.spamserver.services.EntityService;
import net.media.spamserver.services.RequestHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by Satheesh on 8/17/14.
 */
@Controller
public class SpamController {
    @Autowired private RequestHandlerService requestHandlerService;
    @Autowired private BotUaService botUaService;
    @Autowired private ColoListService coloListService;
    @Autowired private EntityService entityService;

    @RequestMapping(value = "/servertest", method = RequestMethod.GET)
    @ResponseBody
    public List<String> serverTest()
    {
        requestHandlerService.testRedis();
        return new ArrayList<>();
    }

    @RequestMapping(value = "/updateBotList", method = RequestMethod.POST)
    @ResponseBody
    public void updateBotList(){
        botUaService.updateBotList();
    }

    @RequestMapping(value = "/spamCheck", method = RequestMethod.GET)
    @ResponseBody
    public Reply spamCheckFinal(@RequestParam Map<String, String> allParameters) {
        ClickDetails clickDetails = new ClickDetailsBuilder().setAdvertiserUrl(
                allParameters.get("advUrl"))
                .setIpStr(allParameters.get("ip"))
                .setVisitID(allParameters.get("visitId"))
                .setUserAgent(allParameters.get("ua"))
                .setPublisherDomain(allParameters.get("pubDomain"))
                .setPublisherUrl(allParameters.get("pubUrl"))
                .setVisitorID(allParameters.get("visitorId"))
                .setPartnerId(allParameters.get("partnerId"))
                .setCustomerId(allParameters.get("customerId"))
                .setCreativeId(allParameters.get("crid"))
                .setTimeTaken(Long.parseLong(allParameters.get("timeTaken")))
                .setPortfolioId(allParameters.get("sectionId"))
                .setInboundIp(allParameters.get("inboundIp"))
                .setParameters(allParameters).buildClickDetails();
        return requestHandlerService.handleClick(clickDetails);
    }

    @RequestMapping(value = "/updateNonBlockingColoRange/{entityId}", method = RequestMethod.POST)
    @ResponseBody
    public void updateNonBlockingColoRange(@PathVariable("entityId") String entityId) {
        coloListService.updateNonBlockingColoRange(entityId);
    }

    @RequestMapping(value = "/updateBlockingColoRange/{entityId}", method = RequestMethod.POST)
    @ResponseBody
    public void updateBlockingColoRange(@PathVariable("entityId") String entityId) {
        coloListService.updateBlockingColoRange(entityId);
    }

    @RequestMapping(value = "/refreshEntity/{isCustomer}/{entityId}", method = RequestMethod.POST)
    @ResponseBody
    public void refreshEntity(@PathVariable("isCustomer") String isCustomer, @PathVariable("entityId") String entityId) {
        entityService.refreshEntityVerifiers(isCustomer.equals("1"), entityId);
    }

    @RequestMapping(value = "/refreshEntities/{isCustomer}", method = RequestMethod.POST)
    @ResponseBody
    public void refreshEntities(@PathVariable("isCustomer") String isCustomer, @RequestBody List<String> entities) {
        entityService.refreshEntitiesVerifier(isCustomer.equals("1"), entities);
    }
}
