package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE_NAME = "config.properties";

    static {
        loadProperties(PROPERTIES_FILE_NAME);
    }

    private static void loadProperties(String propertiesFileName) {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (inputStream == null) {
                LOGGER.error("Property file '{}' not found in the classpath", propertiesFileName);
                throw new RuntimeException("Property file '" + propertiesFileName + "' not found in the classpath");
            }
            properties.load(inputStream);
            LOGGER.info("Properties file '{}' loaded successfully.", propertiesFileName);
        } catch (IOException e) {
            LOGGER.error("Failed to read '{}' file.", propertiesFileName, e);
            throw new RuntimeException("Failed to read '" + propertiesFileName + "' file.", e);
        }
    }

    public static String getProperty(String key) {
        try {
            String property = properties.getProperty(key);
            if (property == null) {
                LOGGER.warn("Property key '{}' not found in the properties file.", key);
                return ""; // Return a default value or handle the null case appropriately
            } else {
                LOGGER.info("Property key '{}' retrieved with value '{}'.", key, property);
                return property;
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred while retrieving property '{}'.", key, e);
            throw e;
        }
    }
}