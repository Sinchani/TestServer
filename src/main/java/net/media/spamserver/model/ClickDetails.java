package net.media.spamserver.model;

import net.media.spamserver.util.BasicUtil;

import java.util.Map;

public class ClickDetails {
    private String ipStr;
    private long ipLong;
    private String userAgent;
    private String customerId;
    private String uaHash;
    private String visitID;
    private String visitorID;
    private String advertiserUrl;
    private String publisherUrl;
    private String publisherDomain;
    private String partnerId;
    private String portfolioId;
    private String creativeId;
    private long timeTaken;
    private String inboundIP;
    private long inBoundIPLong;
    private Map<String, String> parameters;

    public ClickDetails(String ipStr, long ipLong, String userAgent, String uaHash, String advertiserUrl, String customerId,
                        String publisherUrl, String publisherDomain, int timeTaken, String visitID, String visitorID) {
        this.ipStr = ipStr;
        this.ipLong = ipLong;
        this.userAgent = userAgent;
        this.uaHash = uaHash;
        this.advertiserUrl = advertiserUrl;
        this.customerId = customerId;
        this.publisherUrl = publisherUrl;
        this.publisherDomain = publisherDomain;
        this.timeTaken = timeTaken;
        this.visitID = visitID;
        this.visitorID = visitorID;
    }

    public ClickDetails() {}

    public void setInboundIP(String inboundIP) {
        this.inboundIP = inboundIP;
        this.inBoundIPLong = BasicUtil.ipToLong(inboundIP);
    }

    public String getInboundIP() {
        return inboundIP;
    }

    public long getInBoundIPLong() {
        return inBoundIPLong;
    }

    public void setIpLong(long ipLong) {
        this.ipLong = ipLong;
    }

    public void setUaHash(String uaHash) {
        this.uaHash = uaHash;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(String visitorID) {
        this.visitorID = visitorID;
    }

    public String getVisitID() {
        return visitID;
    }

    public void setVisitID(String visitID) {
        this.visitID = visitID;
    }

    public String getIpStr() {
        return ipStr;
    }

    public void setIpStr(String ipStr) {
        this.ipStr = ipStr;
        this.ipLong = BasicUtil.ipToLong(ipStr);
    }

    public long getIpLong() {
        return ipLong;
    }

    public String getUserAgent() {
        if(userAgent==null) return "";
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        this.uaHash = BasicUtil.hash(userAgent);
    }

    public String getAdvertiserUrl() {
        return advertiserUrl;
    }

    public void setAdvertiserUrl(String advertiserUrl) {
        this.advertiserUrl = advertiserUrl;
    }

    public String getPublisherUrl() {
        return publisherUrl;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }

    public String getPublisherDomain() {
        return publisherDomain;
    }

    public void setPublisherDomain(String publisherDomain) {
        this.publisherDomain = publisherDomain;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public long getTimeTaken() {
        if(timeTaken == 0) {
            return 5;
        }
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getStringParam(String key) {
        return parameters.get(key);
    }

    public Integer getIntegerParam(String key) {
        return Integer.parseInt(parameters.get(key));
    }

    public Boolean getBooleanParam(String key) {
        return Boolean.parseBoolean(parameters.get(key));
    }

    public Long getLongParam(String key) {
        return Long.parseLong(parameters.get(key));
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getCreativeId() {
        return creativeId;
    }

    public void setCreativeId(String creativeId) {
        this.creativeId = creativeId;
    }
}
