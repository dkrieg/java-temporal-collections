package com.rifftech.temporal.collections;

public interface TemporalPropertyCollection<T> extends TemporalCollection<T> {
    /**
     * Adds a given item to the temporal collection with an associated time range.
     *
     * @param item the item to be added to the collection; must not be null.
     * @param validTimeRange the temporal range during which the item is considered valid; must not be null and must not overlap with existing ranges.
     */
    void add(T item, TemporalRange validTimeRange);

    /**
     * Deletes a specified item from the temporal collection within the given temporal range.
     *
     * @param item the item to be deleted from the collection; must not be null.
     * @param validTimeRange the temporal range within which the item is considered for deletion; must not be null.
     */
    void delete(T item, TemporalRange validTimeRange);

    /**
     * Deletes all instances of a specified item from the temporal collection across all time ranges.
     *
     * @param item the item to be deleted from the collection; must not be null.
     */
    void deleteAll(T item);
}
