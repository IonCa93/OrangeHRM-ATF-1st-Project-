package utils;

import exceptions.PropertyFileLoadException;
import exceptions.PropertyKeyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static final Properties properties = new Properties();

    private static final String PROPERTIES_FILE_NAME = "config.properties";

    private static final String ENV_VARIABLE_PREFIX = "ORANGEHRM_";

    private static final Set<String> SENSITIVE_PROPERTY_KEYS = Set.of(
            "username",
            "password",
            "client.id",
            "client.secret"
    );

    static {
        loadProperties(PROPERTIES_FILE_NAME);
    }

    private static void loadProperties(String propertiesFileName) {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (inputStream == null) {
                String errorMessage = String.format("Property file '%s' not found in the classpath", propertiesFileName);
                logger.error(errorMessage);
                throw new PropertyFileLoadException(errorMessage);
            }
            properties.load(inputStream);
            logger.info(String.format("Properties file '%s' loaded successfully.", propertiesFileName));
        } catch (IOException e) {
            String errorMessage = String.format("Failed to read '%s' file.", propertiesFileName);
            logger.error(errorMessage, e);
            throw new PropertyFileLoadException(errorMessage, e);
        }
    }

    public static String getProperty(String key) {
        String property = getEnvironmentOverride(key);
        String propertySource = "environment variable";
        if (property == null) {
            property = properties.getProperty(key);
            propertySource = "properties file";
        }

        if (property == null) {
            String errorMessage = String.format("Property key '%s' not found in environment variables or properties file.", key);
            logger.warn(errorMessage);
            throw new PropertyKeyNotFoundException(errorMessage);
        }
        logger.info(String.format(
                "Property key '%s' retrieved from %s with value '%s'.",
                key,
                propertySource,
                getLogSafeValue(key, property)
        ));
        return property;
    }

    private static String getEnvironmentOverride(String key) {
        String environmentVariableName = ENV_VARIABLE_PREFIX + key.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "_");
        String environmentValue = System.getenv(environmentVariableName);
        if (environmentValue == null || environmentValue.isBlank()) {
            return null;
        }
        return environmentValue;
    }

    private static String getLogSafeValue(String key, String value) {
        if (SENSITIVE_PROPERTY_KEYS.contains(key.toLowerCase(Locale.ROOT))) {
            return "<masked>";
        }
        return value;
    }
}
