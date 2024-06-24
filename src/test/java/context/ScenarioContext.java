package context;

import enums.ContextKey;

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
        return instance;//TODO Singleton & scenarioContext (why it's singleton).
    }

    public void saveContext(ContextKey key, Object value) {
        contextMap.put(key, value);
    }

    public <T> T getContext(ContextKey key) {
        Object value = contextMap.get(key);
        if (value != null) {
            try {
                return (T) value;
            } catch (ClassCastException e) {
                throw new ClassCastException(String.format("Unable to cast value for key %s to type %s", key.name(), e.getMessage()));
            }
        }
        return null;
    }

    public void clearContext() {
        contextMap.clear();
    }
}
