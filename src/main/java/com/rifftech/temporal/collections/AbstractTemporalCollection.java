package com.rifftech.temporal.collections;

import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(makeFinal = true, level = PRIVATE)
abstract class AbstractTemporalCollection<T, V extends TemporalValue<T>> implements MutableTemporalCollection<T, V> {
    NavigableMap<Instant, Optional<T>> items = new ConcurrentSkipListMap<>();

    @Override
    public void effectiveAsOfNow(@NonNull T item) {
        effectiveAsOf(Instant.now(), item);
    }

    @Override
    public void expireAsOfNow()  {
        expireAsOf(Instant.now());
    }

    @Override
    public void effectiveAsOf(@NonNull Instant asOf, @NonNull T item) {
        items.put(asOf, Optional.of(item));
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

    protected abstract V temporalValue(@NonNull TemporalRange validTemporalRange, @NonNull T value);
}
