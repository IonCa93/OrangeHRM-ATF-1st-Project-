package utils;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private static ScenarioContext instance;
    private final Map<ScenarioContextKeys.ScenarioContextKey, Object> contextMap;

    private ScenarioContext() {
        contextMap = new HashMap<>();
    }

    public static ScenarioContext getInstance() {
        if (instance == null) {
            instance = new ScenarioContext();
        }
        return instance;
    }

    public void saveValueToScenarioContext(ScenarioContextKeys.ScenarioContextKey key, Object value) {
        contextMap.put(key, value);
    }

    public <T> T getValueFromScenarioContext(ScenarioContextKeys.ScenarioContextKey key) {
        return (T) contextMap.get(key);
    }

    public void clearContext() {
        contextMap.clear();
    }
}
