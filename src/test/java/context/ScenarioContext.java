package context;

import enums.ContextKey;
import exceptions.ContextException;

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

    public void saveContext(ContextKey key, Object value) {
        contextMap.put(key, value);
    }

    public <T> T getContext(ContextKey key, Class<T> clazz) {
        Object value = contextMap.get(key);
        if (value != null) {
            try {
                return clazz.cast(value);
            } catch (ClassCastException e) {
                String message = String.format("Unable to cast value for key '%s' to type '%s'", key.name(), clazz.getName());
                throw new ContextException(message, e);
            }
        }
        return null;
    }

    public void clearContext() {
        contextMap.clear();
    }
}