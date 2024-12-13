package com.rifftech.temporal.collections;

import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiFunction;

import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static lombok.AccessLevel.PRIVATE;

/**
 * Abstract base class for managing a collection of temporal values, where each value is
 * associated with a defined temporal range. This class provides basic functionality for
 * adding, expiring, and retrieving temporal values based on their validity period.
 *
 * The collection maintains the temporal values using a {@link NavigableMap}, with {@link Instant}
 * as keys representing the moments when values become effective or expire and the actual values
 * stored as {@code Optional<T>}. This implementation allows efficient querying and manipulation
 * of temporal values.
 *
 * This class is abstract and requires subclass implementation for the method to create
 * domain-specific temporal value representations.
 *
 * @param <T> The type of the value stored within the collection.
 * @param <V> The type of the temporal value, extending {@link TemporalValue}, representing the
 *            value and its associated temporal range.
 */
@FieldDefaults(makeFinal = true, level = PRIVATE)
abstract class AbstractTemporalCollection<T, V extends TemporalValue<T>> implements MutableTemporalCollection<T, V> {
    NavigableMap<Instant, Optional<T>> items = new ConcurrentSkipListMap<>();

    @Override
    public void effectiveAsOfNow(@NonNull T item) {
        effectiveAsOf(Instant.now(), item);
    }

    @Override
    public void expireAsOfNow() {
        expireAsOf(Instant.now());
    }

    @Override
    public void effectiveAsOf(@NonNull Instant validTime, @NonNull T item) {
        items.put(validTime, Optional.of(item));
    }

    @Override
    public void expireAsOf(@NonNull Instant expireAt) {
        items.put(expireAt, Optional.empty());
    }

    @Override
    public Optional<V> getAsOfNow() {
        return getAsOf(Instant.now());
    }

    @Override
    public Optional<V> getAsOf(Instant validTime) {
        return Optional.ofNullable(validTime)
                .map(items::floorEntry)
                .filter(e -> e.getValue().isPresent())
                .flatMap(e1 -> Optional.ofNullable(items.higherEntry(validTime))
                        .map(e2 -> temporalValue(fromTo(e1.getKey(), e2.getKey()), e1.getValue().get()))
                        .or(() -> Optional.of(temporalValue(fromToMax(e1.getKey()), e1.getValue().get()))));
    }

    @Override
    public Optional<V> getPriorToNow() {
        return getPriorTo(Instant.now());
    }

    @Override
    public Optional<V> getPriorTo(Instant validTime) {
        return Optional.ofNullable(validTime)
                .map(items::floorKey)
                .map(items::lowerEntry)
                .filter(e -> e.getValue().isPresent())
                .map(e1 -> temporalValue(fromTo(e1.getKey(), items.higherKey(e1.getKey())), e1.getValue().get()));
    }

    @Override
    public Collection<V> getInRange(TemporalRange validTimeRange) {
        return Optional.ofNullable(validTimeRange)
                .map(v -> items.subMap(v.start(), true, v.end(), false)
                        .keySet()
                        .stream()
                        .map(this::getAsOf)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList())
                .orElse(Collections.emptyList());
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    Optional<T> compute(Instant validTime, BiFunction<Instant, Optional<T>, Optional<T>> mappingFunction) {
        return items.compute(validTime, mappingFunction);
    }

    protected abstract V temporalValue(@NonNull TemporalRange validTemporalRange, @NonNull T value);
}
