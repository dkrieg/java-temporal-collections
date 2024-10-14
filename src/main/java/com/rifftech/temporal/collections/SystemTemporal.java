package com.rifftech.temporal.collections;

/**
 * Represents an entity that possesses a system-effective temporal range.
 * Entities implementing this interface should be able to provide a time range
 * indicating when the system considers the entity to be valid.
 */
public interface SystemTemporal {
    /**
     * Provides the system-effective temporal range for the entity.
     *
     * @return the TemporalRange during which the system considers the entity to be valid.
     */
    TemporalRange systemEffective();
}
