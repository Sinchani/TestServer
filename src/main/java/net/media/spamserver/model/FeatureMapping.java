package net.media.spamserver.model;

/**
 * Created by vivek on 3/20/15.
 */
public class FeatureMapping {
    private String attribute_name;
    private String attribute_value;

    public String getAttributeName() {
        return attribute_name;
    }

    public void setAttributeName(String attributeName) {
        this.attribute_name = attribute_name;
    }

    public String getAttributeValue() {
        return attribute_value;
    }

    public void setAttributeValue(String attributeValue) {
        this.attribute_value = attribute_value;
    }
}
