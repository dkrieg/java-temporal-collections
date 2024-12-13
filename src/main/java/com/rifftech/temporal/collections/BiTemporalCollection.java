package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a bi-temporal collection that maintains a set of values, each associated
 * with both a valid time range and a transaction time range. This implementation allows
 * operations to manage and query data with two time dimensions.
 *
 * @param <T> the type of value stored within the bi-temporal collection
 */
public class BiTemporalCollection<T> implements MutableBiTemporalCollection<T> {
    BusinessTemporalCollection<SystemTemporalCollection<T>> items = new BusinessTemporalCollection<>();

    @Override
    public void effectiveAsOfNow(@NonNull T item) {
        effectiveAsOf(Instant.now(), item);
    }

    @Override
    public void effectiveAsOf(@NonNull Instant validTime, @NonNull T item) {
        items.compute(validTime, (t, n) -> Optional.ofNullable(n)
                        .orElseGet(() -> Optional.of(new SystemTemporalCollection<>())))
                .ifPresent(c -> c.effectiveAsOfNow(item));
    }

    @Override
    public void effectiveAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime, @NonNull T item) {
        items.compute(validTime, (t, n) -> Optional.ofNullable(n)
                        .orElseGet(() -> Optional.of(new SystemTemporalCollection<>())))
                .ifPresent(c -> c.effectiveAsOf(transactionTime, item));
    }

    @Override
    public void expireAsOfNow() {
        expireAsOf(Instant.now());
    }

    @Override
    public void expireAsOf(@NonNull Instant expireAt) {
        items.expireAsOf(expireAt);
    }

    @Override
    public Optional<BiTemporalValue<T>> getAsOfNow() {
        return items.getAsOfNow()
                .flatMap(v1 -> v1.value()
                        .getAsOfNow()
                        .map(v2 -> biTemporalValue(v1.validTemporalRange(), v2.validTemporalRange(), v2.value())));
    }

    @Override
    public Optional<BiTemporalValue<T>> getAsOf(Instant validTime) {
        return items.getAsOf(validTime)
                .flatMap(v1 -> v1.value()
                        .getAsOfNow()
                        .map(v2 -> biTemporalValue(v1.validTemporalRange(), v2.validTemporalRange(), v2.value())));
    }

    @Override
    public Optional<BiTemporalValue<T>> getAsOf(Instant validTime, Instant transactionTime) {
        return items.getAsOf(validTime)
                .flatMap(v1 -> v1.value()
                        .getAsOf(transactionTime)
                        .map(v2 -> biTemporalValue(v1.validTemporalRange(), v2.validTemporalRange(), v2.value())));
    }

    @Override
    public Optional<BiTemporalValue<T>> getPriorToNow() {
        return items.getPriorToNow()
                .flatMap(v1 -> v1.value()
                        .getAsOfNow()
                        .map(v2 -> biTemporalValue(v1.validTemporalRange(), v2.validTemporalRange(), v2.value())));
    }

    @Override
    public Optional<BiTemporalValue<T>> getPriorTo(Instant validTime) {
        return items.getPriorTo(validTime)
                .flatMap(v1 -> v1.value()
                        .getAsOfNow()
                        .map(v2 -> biTemporalValue(v1.validTemporalRange(), v2.validTemporalRange(), v2.value())));
    }

    @Override
    public Optional<BiTemporalValue<T>> getPriorTo(Instant validTime, Instant transactionTime) {
        return items.getPriorTo(validTime)
                .flatMap(v1 -> v1.value()
                        .getAsOf(transactionTime)
                        .map(v2 -> biTemporalValue(v1.validTemporalRange(), v2.validTemporalRange(), v2.value())));
    }

    @Override
    public Optional<BiTemporalValue<T>> getPriorToPriorTo(Instant validTime, Instant transactionTime) {
        return items.getPriorTo(validTime)
                .flatMap(v1 -> v1.value()
                        .getPriorTo(transactionTime)
                        .map(v2 -> biTemporalValue(v1.validTemporalRange(), v2.validTemporalRange(), v2.value())));
    }

    @Override
    public Collection<BiTemporalValue<T>> getInRange(TemporalRange validTimeRange) {
        return items.getInRange(validTimeRange)
                .stream()
                .map(v1 -> v1.value().getAsOfNow().map(v2 -> biTemporalValue(v1.validTemporalRange(), v2.validTemporalRange(), v2.value())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BiTemporalValue<T>> getInRange(TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        return items.getInRange(validTimeRange)
                .stream()
                .flatMap(v1 -> v1.value().getInRange(transactionTimeRange)
                        .stream()
                        .map(v2 -> biTemporalValue(v1.validTemporalRange(), v2.validTemporalRange(), v2.value())))
                .collect(Collectors.toList());
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    BiTemporalValue<T> biTemporalValue(TemporalRange validTimeRange, TemporalRange transactionTimeRange, T value) {
        return new BiTemporalValue<>(validTimeRange, transactionTimeRange, value);
    }
}
