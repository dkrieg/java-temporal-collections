package com.rifftech.temporal.collections;

/**
 * Represents a collection of bi-temporal objects that can track changes over time
 * in both valid time range and transaction time range. Provides methods for adding,
 * updating, and deleting items from the collection.
 *
 * @param <T> the type of elements in this bi-temporal object collection
 */
public interface BiTemporalObjectCollection<T extends BiTemporal> extends BiTemporalCollection<T> {

    /**
     * Adds an item to the bi-temporal object collection.
     *
     * @param item the item to be added to the collection
     */
    void add(T item);

    /**
     * Deletes an item from the bi-temporal object collection.
     *
     * @param item the item to be deleted from the collection
     */
    void delete(T item);
}
