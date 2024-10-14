package com.rifftech.temporal.collections;

/**
 * Represents a historical record with bi-temporal attributes, including both an item and its associated
 * time range that captures its valid time range and transaction time range.
 *
 * @param <T> the type of the item contained within this bi-temporal historical record
 */
public record BiTemporalHistoricalRecord<T>(
        T item,
        BiTemporalRange timeRange) {
}