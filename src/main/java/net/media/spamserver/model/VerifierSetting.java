package net.media.spamserver.model;

import java.util.List;

/**
 * Created by vivek on 3/5/15.
 */
public class VerifierSetting {
    private String verifierName;
    private int threshold;
    private int expiry;
    private int returnStatus;
    private List<String> keys;
    private boolean sticky;

    public VerifierSetting(String verifierName, int threshold, int expiry, int returnStatus, List<String> keys) {
        this.verifierName = verifierName;
        this.threshold = threshold;
        this.expiry = expiry;
        this.returnStatus = returnStatus;
        this.keys = keys;
    }

    public boolean isSticky() {
        return sticky;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public int getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(int returnStatus) {
        this.returnStatus = returnStatus;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public String getVerifierName() {
        return verifierName;
    }

    public void setVerifierName(String verifierName) {
        this.verifierName = verifierName;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
