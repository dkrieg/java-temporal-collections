package com.rifftech.temporal.collections;

/**
 * NavigableBusinessTemporalObjectCollection is a specialized implementation of a temporal collection that works with objects
 * implementing the BusinessTemporal interface. This collection maintains the validity of objects based on business-effective time ranges.
 *
 * @param <T> the type of BusinessTemporal objects held in this collection
 */
public class NavigableBusinessTemporalObjectCollection<T extends BusinessTemporal> extends BaseTemporalCollection<T> implements BusinessTemporalObjectCollection<T> {
    /**
     * Adds an item to the temporal collection using its business-effective temporal range.
     *
     * @param item the item to be added to the collection; must not be null.
     * @throws IllegalArgumentException if the item is null or if the temporal range is null or overlaps with an existing range.
     */
    @Override
    public void add(T item) {
        add(item, item.businessEffective());
    }


    /**
     * Deletes the specified item from the collection using its business-effective temporal range.
     *
     * @param item the item to be deleted from the collection; must not be null.
     * @throws IllegalArgumentException if the item is null.
     */
    @Override
    public void delete(T item) {
        delete(item, item.businessEffective());
    }
}
