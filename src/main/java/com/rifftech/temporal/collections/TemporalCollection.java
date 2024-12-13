package com.rifftech.temporal.collections;

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
 * @param <V> the type of the temporal value that extends {@link TemporalValue}
 */
public interface TemporalCollection<T, V extends TemporalValue<T>> {
    /**
     * Retrieves the temporal value that is currently valid as of the present moment,
     * if such a value exists.
     *
     * @return an {@code Optional} containing the value valid at the present moment,
     *         or an empty {@code Optional} if no such value exists.
     */
    Optional<V> getAsOfNow();

    /**
     * Retrieves the temporal value that was valid at the specified point in time, if such a value exists.
     *
     * @param validTime the point in time for which to retrieve the valid temporal value.
     *                  This parameter must not be null.
     * @return an {@code Optional} containing the value valid at the specified time,
     *         or an empty {@code Optional} if no such value exists at that time.
     */
    Optional<V> getAsOf(Instant validTime);

    /**
     * Retrieves the temporal value that was valid immediately prior to the present moment,
     * if such a value exists.
     *
     * @return an {@code Optional} containing the most recent value that was valid prior to now,
     *         or an empty {@code Optional} if no such value exists.
     */
    Optional<V> getPriorToNow();

    /**
     * Retrieves the temporal value that was valid immediately prior to the specified point in time,
     * if such a value exists.
     *
     * @param validTime the point in time for which to retrieve the most recent temporal value
     *                  that was valid before it. This parameter must not be null.
     * @return an {@code Optional} containing the most recent value that was valid before
     *         the specified time, or an empty {@code Optional} if no such value exists.
     */
    Optional<V> getPriorTo(Instant validTime);

    /**
     * Retrieves a collection of temporal values that are valid within the specified temporal range.
     *
     * @param validTimeRange the range of time for which to retrieve the valid temporal values.
     *                       This parameter must not be null.
     * @return a collection of temporal values that are valid within the specified range.
     *         If no such values exist, an empty collection is returned.
     */
    Collection<V> getInRange(TemporalRange validTimeRange);

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
