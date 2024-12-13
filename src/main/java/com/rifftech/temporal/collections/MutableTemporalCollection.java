package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;

/**
 * An interface that extends {@link TemporalCollection} and provides methods to
 * modify temporal values within the collection. It allows adding new values,
 * marking values as effective or expired, and managing their temporal validity.
 *
 * @param <T> the type of the value stored within the temporal elements
 * @param <V> the type of the temporal value that extends {@link TemporalValue}
 */
public interface MutableTemporalCollection<T, V extends TemporalValue<T>> extends TemporalCollection<T, V> {
    /**
     * Marks the provided item as effective starting from the current moment in time.
     *
     * @param item the item to be marked as effective. This parameter must not be null.
     */
    void effectiveAsOfNow(@NonNull T item);

    /**
     * Marks the provided item as effective starting at the specified valid time.
     *
     * @param validTime the {@link Instant} representing the time when the item becomes effective. Must not be null.
     * @param item the item to be marked as effective. Must not be null.
     */
    void effectiveAsOf(@NonNull Instant validTime, @NonNull T item);

    /**
     * Marks the currently effective temporal value as expired as of the current moment in time.
     * After this operation, the temporal value in the collection will not be valid as of now.
     */
    void expireAsOfNow();

    /**
     * Marks the temporal value effective as of {@code expireAt} as expired.
     * After this operation, the temporal value in the collection will not be valid starting from the given timestamp.
     *
     * @param expireAt the {@link Instant} representing the time from which the current value is considered expired.
     *                 This parameter must not be null.
     */
    void expireAsOf(@NonNull Instant expireAt);
}
