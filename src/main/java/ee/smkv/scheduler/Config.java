package ee.smkv.scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final String CONFIG_PROPERTIES = "config.properties";
    public static final int CONFIG_LOAD_FAILED_EXIT_CODE = 1;
    public static final Config instance = new Config();

    Properties properties = new Properties();

    Config() {
        try {
            properties.load(getConfigPropertiesFileAsStream());
        } catch (IOException e) {
            System.err.println("Unable to load " + CONFIG_PROPERTIES + " file from classpath: " + e.getMessage());
            System.exit(CONFIG_LOAD_FAILED_EXIT_CODE);
        }
    }

    private InputStream getConfigPropertiesFileAsStream() {
        return getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES);
    }

    public static String getProperty(String name, String defaultValue) {
        return instance.properties.getProperty(name, defaultValue);
    }

    public static String getProperty(String name) {
        return getProperty(name, null);
    }

    public static Long getLongProperty(String name , Long defaultValue){
        try {
            return Long.valueOf(getProperty(name , ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        for (Object key : properties.keySet()) {
            if (builder.length() > 0) builder.append("\n");
            builder.append(key).append('=').append(properties.get(key));
        }
        return builder.toString();
    }
}
