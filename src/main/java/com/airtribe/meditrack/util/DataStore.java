package com.airtribe.meditrack.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic in-memory storage for entities indexed by their identifier.
 *
 * @param <T> the type of entity to store
 */
public class DataStore<T> {
    private final Map<Integer, T> db = new HashMap<>();

    public void add(int key, T value) {
        db.put(key, value);
    }

    public T get(int key) {
        if (db.containsKey(key)) {
            return db.get(key);
        }
        throw new RuntimeException("No data found for key: " + key);
    }

    public void update(int key, T value) {
        db.put(key, value);
    }

    public void delete(int key) {
        db.remove(key);
    }
}
