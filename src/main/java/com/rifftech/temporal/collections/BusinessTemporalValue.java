package com.rifftech.temporal.collections;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents a business-specific temporal value associated with a defined temporal range.
 * This class extends the functionality of TemporalValue by inheriting its ability to represent
 * and compare values based on their valid temporal range. It is designed to handle cases
 * where a temporal value has additional business context or constraints.
 *
 * @param <T> The type of the value associated with the temporal range.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BusinessTemporalValue<T> extends TemporalValue<T> {
    BusinessTemporalValue(TemporalRange validTemporalRange, T value) {
        super(validTemporalRange, value);
    }
}
