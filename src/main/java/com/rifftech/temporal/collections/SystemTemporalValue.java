package com.rifftech.temporal.collections;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SystemTemporalValue<T> extends TemporalValue<T> {
    SystemTemporalValue(TemporalRange validTemporalRange, T value) {
        super(validTemporalRange, value);
    }
}
