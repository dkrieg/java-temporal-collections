package com.rifftech.temporal.collections;

import lombok.NonNull;

/**
 * Represents a generic value associated with a specific temporal range.
 * This abstract class ensures that any subclass can define the nature
 * of its value while maintaining a valid temporal range.
 *
 * @param <T> The type of the value this class holds.
 *            The temporal range defines the time period during which the value is considered valid.
 *            Subclasses can extend this functionality to accommodate more specific temporal behaviors.
 */
public record TemporalRecord<T>(@NonNull TemporalRange validRange, @NonNull T value) implements Comparable<TemporalRecord<T>> {

    @Override
    public int compareTo(TemporalRecord<T> that) {
        return this.validRange.compareTo(that.validRange);
    }
}
