package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;
import java.util.Optional;

/**
 * An interface that extends {@link TemporalCollection} and provides methods to
 * modify temporal values within the collection. It allows adding new values,
 * marking values as effective or expired, and managing their temporal validity.
 *
 * @param <T> the type of the value stored within the temporal elements
 */
public interface MutableTemporalCollection<T> extends TemporalCollection<T> {
    /**
     * Associates the specified item with the current moment.
     *
     * @param item the item for which the currently valid temporal value is retrieved.
     *             Must not be null.
     * @return an {@code Optional} containing the previously effective temporal value ,
     * or an empty {@code Optional} if no such value exists.
     */
    default Optional<TemporalRecord<T>> effectiveAsOfNow(@NonNull T item) {
        return effectiveAsOf(Instant.now(), item);
    }

    /**
     * Marks the specified item as effective at the given valid time, updating the internal
     * temporal collection state accordingly.
     * If an item already exists the given valid time, its value is replaced and returned with no history of this
     * modification being made. Otherwise, the item is made effective at the given valid time and the prior,
     * now expired, version is returned.
     *
     * @param validTime the time at which the item should be marked as effective.
     *                  Must not be null.
     * @param item the item to be marked as effective at the specified valid time.
     *             Must not be null.
     * @return an {@code Optional} containing the prior temporal value that was effective
     *         at the specified valid time, or an empty {@code Optional} if no prior
     *         value was present.
     */
    Optional<TemporalRecord<T>> effectiveAsOf(@NonNull Instant validTime, @NonNull T item);

    /**
     * Marks the currently effective temporal value in the collection as expired as of the present moment.
     * This method is a convenience wrapper around {@code expireAsOf} with the current instant as the parameter.
     *
     * @return an {@code Optional} containing the temporal record that was previously effective
     *         and has now been marked as expired, or an empty {@code Optional}
     *         if no such value exists.
     */
    default Optional<TemporalRecord<T>> expireAsOfNow() {
        return expireAsOf(Instant.now());
    }

    /**
     * Marks the currently effective temporal value in the collection as expired as of the specified timestamp.
     * If the provided timestamp matches an entry in the collection, that entry is marked as expired. If the
     * timestamp does not match an existing entry, the temporal value effective immediately prior to the
     * provided timestamp is marked as expired.
     *
     * @param expireAt the timestamp at which the temporal value should be marked as expired. Must not be null.
     * @return an {@code Optional} containing the temporal record that was previously effective
     *         and has now been marked as expired, or an empty {@code Optional} if no such value exists.
     */
    Optional<TemporalRecord<T>> expireAsOf(@NonNull Instant expireAt);
}
