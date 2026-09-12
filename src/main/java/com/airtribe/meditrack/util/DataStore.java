package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.MedicalEntity;
import com.airtribe.meditrack.exception.InvalidDataException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generic in-memory storage for MediTrack entities indexed by their identifier.
 *
 * @param <T> the type of entity to store
 */
public class DataStore<T extends MedicalEntity> {
    private final Map<Integer, T> db = new LinkedHashMap<>();

    public void add(T value) {
        if (value == null) {
            throw new InvalidDataException("Stored value must not be null.");
        }
        if (db.containsKey(value.getId())) {
            throw new InvalidDataException("An entity already exists for id: " + value.getId());
        }
        db.put(value.getId(), value);
    }

    public Optional<T> findById(int id) {
        return Optional.ofNullable(db.get(id));
    }

    public T get(int id, String entityName) {
        return findById(id).orElseThrow(
                () -> new InvalidDataException(entityName + " not found for id: " + id));
    }

    public void update(T value) {
        if (value == null) {
            throw new InvalidDataException("Stored value must not be null.");
        }
        if (!db.containsKey(value.getId())) {
            throw new InvalidDataException("Cannot update a missing entity with id: " + value.getId());
        }
        db.put(value.getId(), value);
    }

    public boolean delete(int id) {
        return db.remove(id) != null;
    }

    public List<T> findAll() {
        return List.copyOf(new ArrayList<>(db.values()));
    }

    public boolean exists(int id) {
        return db.containsKey(id);
    }

    public int size() {
        return db.size();
    }
}
