package com.rifftech.temporal.collections;

import lombok.NonNull;

/**
 * BusinessTemporalCollection is a collection that manages temporal values with associated
 * temporal ranges, specifically tailored for business use cases. It extends the functionality
 * provided by AbstractTemporalCollection to include business-specific temporal values by using
 * BusinessTemporalValue as the representation of its elements.
 *
 * @param <T> The type of the value associated with the temporal range.
 */
public class BusinessTemporalCollection<T> extends AbstractTemporalCollection<T, BusinessTemporalValue<T>> {
    @Override
    protected BusinessTemporalValue<T> temporalValue(@NonNull TemporalRange validTemporalRange, @NonNull T value) {
        return new BusinessTemporalValue<>(validTemporalRange, value);
    }
}
