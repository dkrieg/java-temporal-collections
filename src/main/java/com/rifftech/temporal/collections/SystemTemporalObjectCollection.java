package com.rifftech.temporal.collections;

/**
 * Represents a collection of SystemTemporal objects where each object is associated
 * with a specific system-effective temporal range.
 * <p>
 * This interface extends TemporalObjectCollection<SystemTemporal>, thereby inheriting
 * methods for adding, updating, and deleting items, as well as retrieving items based on
 * their temporal validity.
 */
public interface SystemTemporalObjectCollection<T extends SystemTemporal> extends TemporalObjectCollection<T> {
}
