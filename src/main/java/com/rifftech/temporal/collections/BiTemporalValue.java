package com.rifftech.temporal.collections;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

/**
 * Represents a bi-temporal value, extending the concept of {@link TemporalValue}
 * by introducing an additional temporal dimension.
 * Alongside the valid temporal range (defining when the value is applicable),
 * this class includes a transaction temporal range which represents
 * the time period during which the value was recorded or is valid in terms of transactions.
 *
 * @param <T> The type of the value this class holds.
 *
 * This class is intended to handle scenarios where both validity and transaction
 * time are important, such as in bi-temporal databases or systems
 * handling historical and transactional data.
 *
 * The {@code validTemporalRange} defines when the value is valid,
 * while the {@code transactionTemporalRange} represents the time frame
 * during which the value existed in the system or database from a transactional perspective.
 */
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
