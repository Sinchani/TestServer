package net.media.spamserver.config;

import java.net.URL;
import java.util.Properties;

public class SysProperties {
    private static Properties prop = null;

    private SysProperties() {
    }

    private static String getPropertiesFile(){
        return "development.properties";
    }

    private static ClassLoader getClassLoader(){
        ClassLoader loader = SysProperties.class.getClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        return loader;
    }

    public static Properties getInstance() {
        if (prop == null) {
            ClassLoader loader = getClassLoader();
            String propFile = getPropertiesFile();
            try {
                URL url = loader.getResource(propFile);
                prop = new Properties();
                prop.load((url.openStream()));
            }
            catch (Exception ex) {}
        }
        return prop;
    }
}

