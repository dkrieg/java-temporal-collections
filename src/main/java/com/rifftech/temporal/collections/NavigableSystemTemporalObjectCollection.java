package com.rifftech.temporal.collections;

/**
 * A collection of SystemTemporal objects that supports navigation within a temporal range.
 * This class enables adding and deleting items based on their system-effective temporal range.
 *
 * @param <T> the type of SystemTemporal object contained in the collection.
 */
public class NavigableSystemTemporalObjectCollection<T extends SystemTemporal> extends BaseTemporalCollection<T> implements SystemTemporalObjectCollection<T> {
    /**
     * Adds a given item to the temporal collection based on its system-effective temporal range.
     *
     * @param item the item to be added to the collection; must not be null.
     * @throws IllegalArgumentException if the item is null or if its system-effective
     * temporal range is null or overlaps with an existing range.
     */
    @Override
    public void add(T item) {
        add(item, item.systemEffective());
    }

    /**
     * Deletes the specified item from the collection based on its system-effective temporal range.
     *
     * @param item the item to be deleted from the collection; must not be null.
     * @throws IllegalArgumentException if the item is null.
     */
    @Override
    public void delete(T item) {
        delete(item, item.systemEffective());
    }
}
