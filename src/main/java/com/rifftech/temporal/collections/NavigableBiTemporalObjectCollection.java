package com.rifftech.temporal.collections;

/**
 * A collection class that extends BaseBiTemporalCollection and implements the BusinessTemporalObjectCollection interface.
 * This class provides functionality for managing temporal objects in a bi-temporal manner.
 *
 */
public class NavigableBiTemporalObjectCollection<T extends BiTemporal> extends BaseBiTemporalCollection<T> implements BiTemporalObjectCollection<T> {
    /**
     * Adds an item to the bi-temporal collection. The item is added with its business-effective and system-effective
     * temporal ranges.
     *
     * @param item the item to be added to the collection, must not be null. It implements the BiTemporal interface and
     *             contains information about its business-effective and system-effective time ranges
     */
    @Override
    public void add(T item) {
        add(item, item.businessEffective(), item.systemEffective());
    }

    /**
     * Deletes an item from the bi-temporal collection. The item to be deleted
     * will have its business-effective and system-effective temporal ranges evaluated
     * for removal.
     *
     * @param item the item to be deleted from the collection, must not be null. It implements the
     *             BiTemporal interface and contains information about its business-effective
     *             and system-effective time ranges.
     */
    @Override
    public void delete(T item) {
        delete(item, item.businessEffective(), item.systemEffective());
    }
}
