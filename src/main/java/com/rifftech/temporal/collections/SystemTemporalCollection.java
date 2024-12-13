package com.rifftech.temporal.collections;

import lombok.NonNull;

/**
 * The SystemTemporalCollection class provides a specialized implementation of the
 * AbstractTemporalCollection for managing temporal values within a system-defined context.
 *
 * This class stores and manipulates temporal data in the form of SystemTemporalValue instances,
 * which associate a value of type {@code T} with a specific temporal range.
 * It is capable of handling operations such as adding, expiring, and querying values based on
 * their temporal validity.
 *
 * The primary functionality of this class is achieved by overriding the {@code temporalValue}
 * method to construct SystemTemporalValue instances supported by the provided
 * TemporalRange and value.
 *
 * @param <T> The type of the values being managed within this temporal collection.
 */
public class SystemTemporalCollection<T> extends AbstractTemporalCollection<T, SystemTemporalValue<T>> {
    @Override
    protected SystemTemporalValue<T> temporalValue(@NonNull TemporalRange validTemporalRange, @NonNull T value) {
        return new SystemTemporalValue<>(validTemporalRange, value);
    }
}
