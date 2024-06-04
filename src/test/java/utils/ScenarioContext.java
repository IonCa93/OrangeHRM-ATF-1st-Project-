package utils;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private static ScenarioContext instance;
    private final Map<ContextKey, Object> contextMap;

    private ScenarioContext() {
        contextMap = new HashMap<>();
    }

    public static ScenarioContext getInstance() {
        if (instance == null) {
            instance = new ScenarioContext();
        }
        return instance;
    }

    public void saveValueToScenarioContext(ContextKey key, Object value) {
        contextMap.put(key, value);
    }

    public <T> T getValueFromScenarioContext(ContextKey key) {
        return (T) contextMap.get(key);
    }

    public void clearContext() {
        contextMap.clear();
    }
}
