package com.parasoft.parabank.utility;

import com.parasoft.parabank.controller.LogController;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores scenario-specific data in a thread-safe map.
 * Provides type-safe and generic get/set methods.
 */
public class ScenarioContext {
    private final Map<String, Object> data = new HashMap<>();

    /**
     * Stores a value with the specified key.
     *
     * @param key   the key to associate with the value
     * @param value the value to store
     */
    public void set(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Retrieves a value by key and casts it to the specified type.
     * Returns null if the key is not present or the type does not match.
     *
     * @param key   the key to look up
     * @param clazz the expected class type
     * @param <T>   the type parameter
     * @return the value cast to type T, or null if not present or type mismatch
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = data.get(key);
        if (value == null) return null;
        try {
            return clazz.cast(value);
        } catch (ClassCastException e) {
            // Log the error and return null
            LogController.error(
                "Type mismatch for key '" + key + "': " + e.getMessage()
            );
            return null;
        }
    }

    /**
     * Retrieves a value by key.
     *
     * @param key the key to look up
     * @return the value, or null if not present
     */
    public Object get(String key) {
        return data.get(key);
    }
}
