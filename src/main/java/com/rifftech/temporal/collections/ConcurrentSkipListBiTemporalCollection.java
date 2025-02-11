package com.rifftech.temporal.collections;

import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.rifftech.temporal.collections.TemporalRange.MAX;
import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ConcurrentSkipListBiTemporalCollection<T> implements MutableBiTemporalCollection<T> {
    ConcurrentSkipListMap<Instant, ConcurrentSkipListMap<Instant, Optional<T>>> items = new ConcurrentSkipListMap<>();

    @Override
    public Optional<BiTemporalRecord<T>> effectiveAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime, @NonNull T item) {
        final Optional<BiTemporalRecord<T>> priorValue = getAsOf(validTime, transactionTime);
        if (items.containsKey(validTime)) {
            items.get(validTime).put(transactionTime, Optional.of(item));
        } else {
            ConcurrentSkipListMap<Instant, Optional<T>> map = new ConcurrentSkipListMap<>();
            map.put(transactionTime, Optional.of(item));
            items.put(validTime, map);
        }
        return priorValue;
    }

    @Override
    public Optional<BiTemporalRecord<T>> expireAsOf(@NonNull Instant businessTime, @NonNull Instant systemTime) {
        if (isEmpty()) {
            return Optional.empty();
        } else {
            final Optional<BiTemporalRecord<T>> priorValue = getAsOf(businessTime, systemTime);
            if (items.containsKey(businessTime)) {
                ConcurrentSkipListMap<Instant, Optional<T>> map = items.get(businessTime);
                if (!map.isEmpty()) {
                    map.put(systemTime, Optional.empty());
                }
            } else {
                items.put(businessTime, new ConcurrentSkipListMap<>());
            }
            return priorValue;
        }
    }

    @Override
    public Optional<BiTemporalRecord<T>> getAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime) {
        return Optional.of(validTime)
                .map(items::floorEntry)
                .flatMap(businessStartEntry -> Optional.ofNullable(items.higherKey(businessStartEntry.getKey()))
                        .or(() -> Optional.of(MAX))
                        .map(businessEnd -> Optional.of(transactionTime)
                                .map(businessStartEntry.getValue()::floorEntry)
                                .filter(e -> e.getValue().isPresent())
                                .flatMap(systemStartEntry -> Optional.ofNullable(businessStartEntry.getValue().higherKey(systemStartEntry.getKey()))
                                        .or(() -> Optional.of(MAX))
                                        .map(systemEnd -> new BiTemporalRecord<>(
                                                fromTo(businessStartEntry.getKey(), businessEnd),
                                                fromTo(systemStartEntry.getKey(), systemEnd),
                                                systemStartEntry.getValue().get())))))
                .flatMap(Function.identity());
    }

    @Override
    public Optional<BiTemporalRecord<T>> getPriorTo(@NonNull Instant validTime, @NonNull Instant transactionTime) {
        return Optional.of(validTime)
                .map(items::floorKey)
                .map(items::lowerEntry)
                .flatMap(businessStartEntry -> Optional.ofNullable(items.higherKey(businessStartEntry.getKey()))
                        .or(() -> Optional.of(MAX))
                        .map(businessEnd -> Optional.of(transactionTime)
                                .map(businessStartEntry.getValue()::floorEntry)
                                .filter(e -> e.getValue().isPresent())
                                .flatMap(systemStartEntry -> Optional.ofNullable(businessStartEntry.getValue().higherKey(systemStartEntry.getKey()))
                                        .or(() -> Optional.of(MAX))
                                        .map(systemEnd -> new BiTemporalRecord<>(
                                                fromTo(businessStartEntry.getKey(), businessEnd),
                                                fromTo(systemStartEntry.getKey(), systemEnd),
                                                systemStartEntry.getValue().get())))))
                .flatMap(Function.identity());
    }

    @Override
    public Collection<BiTemporalRecord<T>> getInRange(@NonNull TemporalRange validRange) {
        return Optional.of(validRange)
                .map(v -> items.subMap(v.start(), true, v.end(), true)
                        .entrySet()
                        .stream()
                        .map(businessTimeEntry -> getAsOf(businessTimeEntry.getKey(), businessTimeEntry.getValue().firstKey()))
                        .filter(Optional::isPresent)
                        .map(Optional::get))
                .map(Stream::toList)
                .orElse(Collections.emptyList());
    }

    @Override
    public Collection<BiTemporalRecord<T>> getInRange(@NonNull TemporalRange validRange, @NonNull TemporalRange transactionRange) {
        return Optional.of(validRange)
                .map(v -> items.subMap(v.start(), true, v.end(), true)
                        .entrySet()
                        .stream()
                        .map(businessTimeEntry -> businessTimeEntry.getValue().subMap(transactionRange.start(), true, transactionRange.end(), true)
                                .keySet()
                                .stream()
                                .map(systemTime -> getAsOf(businessTimeEntry.getKey(), systemTime))
                                .filter(Optional::isPresent)
                                .map(Optional::get)))
                .map(stream -> stream.flatMap(Function.identity()).toList())
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
