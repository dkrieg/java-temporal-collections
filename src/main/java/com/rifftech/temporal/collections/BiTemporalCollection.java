package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Retrieves the BiTemporalValue associated with the time combination
     * that immediately precedes the given validTime and transactionTime.
     * @see #getPriorTo(Instant, Instant)
     *
     * @param validTime the valid time used as a reference point for the lookup.
     *                  This indicates the effective date of the value.
     * @param transactionTime the transaction time used as a reference point for the lookup.
     *                        This indicates when the value was recorded or changed.
     * @return an Optional containing the BiTemporalValue found prior to the given validTime and transactionTime,
     *         or an empty Optional if no such value exists.
     */
    public Optional<BiTemporalValue<T>> getPriorToAsOf(Instant validTime, Instant transactionTime) {
        return getPriorTo(validTime, transactionTime);
    }

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
