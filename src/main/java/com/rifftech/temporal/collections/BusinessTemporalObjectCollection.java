package com.rifftech.temporal.collections;

/**
 * Represents a collection of BusinessTemporal objects.
 * The collection provides methods to add, update, and delete BusinessTemporal items,
 * as well as to query the collection for items valid at specific times or within specific temporal ranges.
 */
public interface BusinessTemporalObjectCollection<T extends BusinessTemporal> extends TemporalObjectCollection<T>{
}
