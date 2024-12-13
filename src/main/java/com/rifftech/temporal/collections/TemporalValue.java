package com.rifftech.temporal.collections;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

/**
 * Represents a generic value associated with a specific temporal range.
 * This abstract class ensures that any subclass can define the nature
 * of its value while maintaining a valid temporal range.
 *
 * @param <T> The type of the value this class holds.
 * The temporal range defines the time period during which the value is considered valid.
 * Subclasses can extend this functionality to accommodate more specific temporal behaviors.
 */
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString
@FieldDefaults(makeFinal = true, level = PRIVATE)
abstract class TemporalValue<T> implements Comparable<TemporalValue<T>> {
    TemporalRange validTemporalRange;
    T value;

    protected TemporalValue(TemporalRange validTemporalRange, T value) {
        this.validTemporalRange = validTemporalRange;
        this.value = value;
    }

    @Override
    public int compareTo(TemporalValue<T> that) {
        return this.validTemporalRange.compareTo(that.validTemporalRange);
    }
}
