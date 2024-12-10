package com.rifftech.temporal.collections;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Accessors(fluent = true)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class BiTemporalValue<T> extends TemporalValue<T> {
    TemporalRange transactionTemporalRange;

    BiTemporalValue(TemporalRange validTemporalRange, TemporalRange transactionTemporalRange, T value) {
        super(validTemporalRange, value);
        this.transactionTemporalRange = transactionTemporalRange;
    }
}
