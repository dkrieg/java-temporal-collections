package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * An interface representing a collection of temporal values, which are associated
 * with specific temporal ranges. This collection allows querying values based
 * on temporal criteria such as the present moment, a specific timestamp, or
 * a range of time.
 *
 * @param <T> the type of the value stored within the temporal elements
 */
public interface TemporalCollection<T> {
    /**
     * Retrieves the temporal value that is currently valid as of the present moment,
     * if such a value exists.
     *
     * @return an {@code Optional} containing the value valid at the present moment,
     *         or an empty {@code Optional} if no such value exists.
     */
    default Optional<TemporalRecord<T>> getAsOfNow() {
        return getAsOf(Instant.now());
    }

    /**
     * Retrieves the temporal value that was valid at the specified point in time, if such a value exists.
     *
     * @param validTime the point in time for which to retrieve the valid temporal value.
     *                  This parameter must not be null.
     * @return an {@code Optional} containing the value valid at the specified time,
     *         or an empty {@code Optional} if no such value exists at that time.
     */
    Optional<TemporalRecord<T>> getAsOf(@NonNull Instant validTime);

    /**
     * Retrieves the temporal value that was valid immediately prior to the present moment,
     * if such a value exists.
     *
     * @return an {@code Optional} containing the most recent value that was valid prior to now,
     *         or an empty {@code Optional} if no such value exists.
     */
    default Optional<TemporalRecord<T>> getPriorToNow() {
        return getPriorTo(Instant.now());
    }

    /**
     * Retrieves the temporal value that was valid immediately prior to the specified point in time,
     * if such a value exists.
     *
     * @param validTime the point in time for which to retrieve the most recent temporal value
     *                  that was valid before it. This parameter must not be null.
     * @return an {@code Optional} containing the most recent value that was valid before
     *         the specified time, or an empty {@code Optional} if no such value exists.
     */
    Optional<TemporalRecord<T>> getPriorTo(@NonNull Instant validTime);

    /**
     * Retrieves a collection of temporal values that are valid within the specified temporal range.
     *
     * @param validRange the range of time for which to retrieve the valid temporal values.
     *                       This parameter must not be null.
     * @return a collection of temporal values that are valid within the specified range.
     * If no such values exist, an empty collection is returned.
     */
    Collection<TemporalRecord<T>> getInRange(@NonNull TemporalRange validRange);

    /**
     * Returns the number of temporal values currently stored in the collection.
     *
     * @return the number of temporal values in the collection
     */
    int size();

    /**
     * Checks if the collection is empty, meaning it contains no temporal values.
     *
     * @return true if the collection has no temporal values; false otherwise
     */
    boolean isEmpty();
}
