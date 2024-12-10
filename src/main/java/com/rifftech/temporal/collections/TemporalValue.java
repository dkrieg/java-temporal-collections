package com.rifftech.temporal.collections;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

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
