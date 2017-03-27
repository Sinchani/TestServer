package net.media.spamserver.model;

import net.media.spamserver.util.BasicUtil;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by vivek on 7/21/15.
 */
public class ClickDetailsBuilder {
    private static ClickDetails clickDetails;
    private Logger logger;

    private ClickDetails getInstance() {
        if(clickDetails == null) {
            clickDetails = new ClickDetails();
        }
        return clickDetails;
    }

    public ClickDetailsBuilder setInboundIp(String inboundIp) {
        getInstance().setInboundIP(inboundIp);
        return this;
    }

    public ClickDetailsBuilder setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public ClickDetailsBuilder setCustomerId(String customerId) {
        getInstance().setCustomerId(customerId);
        return this;
    }

    public ClickDetailsBuilder setVisitorID(String visitorID) {
        getInstance().setVisitorID(visitorID);
        return this;
    }

    public ClickDetailsBuilder setVisitID(String visitID) {
        getInstance().setVisitID(visitID);
        return this;
    }

    public ClickDetailsBuilder setIpStr(String ipStr) {
        getInstance().setIpStr(ipStr);
        getInstance().setIpLong(BasicUtil.ipToLong(ipStr));
        return this;
    }

    public ClickDetailsBuilder setUserAgent(String userAgent) {
        getInstance().setUserAgent(userAgent);
        getInstance().setUaHash(BasicUtil.hash(userAgent));
        return this;
    }

    public ClickDetailsBuilder setAdvertiserUrl(String advertiserUrl) {
        getInstance().setAdvertiserUrl(advertiserUrl);
        return this;
    }

    public ClickDetailsBuilder setPublisherUrl(String publisherUrl) {
        getInstance().setPublisherUrl(publisherUrl);
        return this;
    }

    public ClickDetailsBuilder setPublisherDomain(String publisherDomain) {
        getInstance().setPublisherDomain(publisherDomain);
        return this;
    }

    public ClickDetailsBuilder setPartnerId(String partnerId) {
        getInstance().setPartnerId(partnerId);
        return this;
    }

    public ClickDetailsBuilder setTimeTaken(long timeTaken) {
        getInstance().setTimeTaken(timeTaken);
        return this;
    }

    public ClickDetailsBuilder setCreativeId(String creativeId) {
        getInstance().setCreativeId(creativeId);
        return this;
    }

    public ClickDetailsBuilder setPortfolioId(String portfolioId) {
        getInstance().setPortfolioId(portfolioId);
        return this;
    }

    public ClickDetailsBuilder setParameters(Map<String, String> parameters) {
        getInstance().setParameters(parameters);
        return this;
    }

    public ClickDetails buildClickDetails() {
        ClickDetails clickDetails = getInstance();
        return sanitize(clickDetails);
    }

    public ClickDetails sanitize(ClickDetails clickDetails) {
        clickDetails.setIpStr(clickDetails.getIpStr() == null ? "" : clickDetails.getIpStr());
        try {
            clickDetails.setIpLong(BasicUtil.ipToLong(clickDetails.getIpStr()));
        } catch (Exception e) {
            //TODO logger.info("Exception setting long IP: " + e.getMessage());
            clickDetails.setIpLong(0);
        }
        clickDetails.setPartnerId(clickDetails.getPartnerId() == null ? "" : clickDetails.getPartnerId());
        clickDetails.setUserAgent(clickDetails.getUserAgent() == null ? "" : clickDetails.getUserAgent());
        clickDetails.setPublisherUrl(clickDetails.getPublisherUrl() == null ? "" : clickDetails.getPublisherUrl());
        clickDetails.setPublisherDomain(clickDetails.getPublisherDomain() == null ? "" : clickDetails.getPublisherDomain());
        clickDetails.setAdvertiserUrl(clickDetails.getAdvertiserUrl() == null ? "" : clickDetails.getAdvertiserUrl());
        clickDetails.setVisitorID(clickDetails.getVisitorID() == null ? "" : clickDetails.getVisitorID());
        clickDetails.setVisitID(clickDetails.getVisitID() == null ? "" : clickDetails.getVisitID());
        clickDetails.setCustomerId(clickDetails.getCustomerId() == null ? "" : clickDetails.getCustomerId());
        clickDetails.setPortfolioId(clickDetails.getPortfolioId() == null ? "" : clickDetails.getPortfolioId());
        clickDetails.setCreativeId(clickDetails.getCreativeId() == null ? "" : clickDetails.getCreativeId());
        return clickDetails;
    }
}
