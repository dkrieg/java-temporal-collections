package com.rifftech.temporal.collections;

public interface TemporalObjectCollection<T> extends TemporalCollection<T> {
    /**
     * Adds a given SystemTemporal item to the collection.
     *
     * @param item the SystemTemporal item to be added; must not be null.
     */
    void add(T item);

    /**
     * Deletes a given SystemTemporal item from the collection.
     *
     * @param item the SystemTemporal item to be deleted; must not be null.
     */
    void delete(T item);
}
