package com.rifftech.temporal.collections;

import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

import static com.rifftech.temporal.collections.TemporalRange.MAX;
import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ConcurrentSkipListTemporalCollection<T> implements MutableTemporalCollection<T> {
    ConcurrentSkipListMap<Instant, Optional<T>> items = new ConcurrentSkipListMap<>();

    @Override
    public Optional<TemporalRecord<T>> effectiveAsOf(@NonNull Instant validTime, @NonNull T item) {
        Optional<TemporalRecord<T>> priorValue;
        if(items.containsKey(validTime)) {
            priorValue = getAsOf(validTime);
            items.put(validTime, Optional.of(item));
        } else {
            items.put(validTime, Optional.of(item));
            priorValue = getPriorTo(validTime);
        }
        return priorValue;
    }

    @Override
    public Optional<TemporalRecord<T>> expireAsOf(@NonNull Instant expireAt) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            Optional<TemporalRecord<T>> priorValue;
            if(items.containsKey(expireAt)) {
                priorValue = getAsOf(expireAt);
                items.put(expireAt, Optional.empty());
            } else {
                items.put(expireAt, Optional.empty());
                priorValue = getPriorTo(expireAt);
            }
            return priorValue;
        }
    }

    @Override
    public Optional<TemporalRecord<T>> getAsOf(@NonNull Instant validTime) {
        return Optional.of(validTime)
                .map(items::floorEntry)
                .filter(e -> e.getValue().isPresent())
                .flatMap(validStartKey -> Optional.ofNullable(items.higherKey(validStartKey.getKey()))
                        .or(() -> Optional.of(MAX))
                        .map(validEnd -> new TemporalRecord<>(
                                fromTo(validStartKey.getKey(), validEnd),
                                validStartKey.getValue().get())));
    }

    @Override
    public Optional<TemporalRecord<T>> getPriorTo(@NonNull Instant validTime) {
        return Optional.of(validTime)
                .map(items::floorKey)
                .map(items::lowerEntry)
                .filter(e -> e.getValue().isPresent())
                .map(validStartKey -> new TemporalRecord<>(
                        fromTo(validStartKey.getKey(), Optional.ofNullable(items.higherKey(validStartKey.getKey())).orElse(MAX)),
                        validStartKey.getValue().get()));
    }

    @Override
    public Collection<TemporalRecord<T>> getInRange(@NonNull TemporalRange validRange) {
        return Optional.of(validRange)
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
}
