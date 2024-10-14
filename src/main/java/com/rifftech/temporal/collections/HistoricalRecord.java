package com.rifftech.temporal.collections;

/**
 * Represents a historical record of an item with a specific temporal range.
 *
 * @param <T> the type of the item being recorded
 * @param item the item for which the historical record is created
 * @param validTimeRange the temporal range during which the record is valid
 */
public record HistoricalRecord<T>(T item, TemporalRange validTimeRange) {
}
