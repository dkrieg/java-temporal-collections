package com.rifftech.temporal.collections;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A concrete implementation of the {@link TemporalValue} class, representing
 * a value associated with a specific temporal range within a system-defined context.
 *
 * This class inherits the capabilities of {@link TemporalValue}, allowing it to hold
 * a value of type {@code T} alongside the temporal range during which the value is valid.
 * It is designed to be used in system-specific applications where temporal values
 * need to be managed.
 *
 * @param <T> The type of value this class holds.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SystemTemporalValue<T> extends TemporalValue<T> {
    SystemTemporalValue(TemporalRange validTemporalRange, T value) {
        super(validTemporalRange, value);
    }
}
