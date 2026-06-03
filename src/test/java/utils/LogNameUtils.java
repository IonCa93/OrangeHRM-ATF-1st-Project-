package utils;

import io.cucumber.java.Scenario;

public class LogNameUtils {

    public static String extractFeatureName(Scenario scenario) {
        String featureName = scenario.getUri().getPath();
        return featureName.substring(featureName.lastIndexOf('/') + 1).replace(".feature", "");
    }
}
