package com.rifftech.temporal.collections;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BusinessTemporalValue<T> extends TemporalValue<T> {
    BusinessTemporalValue(TemporalRange validTemporalRange, T value) {
        super(validTemporalRange, value);
    }
}
