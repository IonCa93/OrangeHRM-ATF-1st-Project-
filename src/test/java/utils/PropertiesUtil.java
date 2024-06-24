package utils;

import exceptions.PropertyFileLoadException;
import exceptions.PropertyKeyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE_NAME = "config.properties";

    static {
        loadProperties(PROPERTIES_FILE_NAME);
    }

    private static void loadProperties(String propertiesFileName) {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (inputStream == null) {
                String errorMessage = String.format("Property file '%s' not found in the classpath", propertiesFileName);
                logger.error(errorMessage);
                throw new PropertyFileLoadException(errorMessage); // Throw PropertyFileLoadException for file not found
            }
            properties.load(inputStream);
            logger.info(String.format("Properties file '%s' loaded successfully.", propertiesFileName));
        } catch (IOException e) {
            String errorMessage = String.format("Failed to read '%s' file.", propertiesFileName);
            logger.error(errorMessage, e);
            throw new PropertyFileLoadException(errorMessage, e); // Throw PropertyFileLoadException for IO issues
        }
    }

    public static String getProperty(String key) {
        String property = properties.getProperty(key);
        if (property == null) {
            String errorMessage = String.format("Property key '%s' not found in the properties file.", key);
            logger.warn(errorMessage);
            throw new PropertyKeyNotFoundException(errorMessage); // Throw PropertyKeyNotFoundException for key not found
        }
        logger.info(String.format("Property key '%s' retrieved with value '%s'.", key, property));
        return property;
    }
}
