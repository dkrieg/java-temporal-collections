package com.rifftech.temporal.collections;

/**
 * The BusinessTemporal interface represents an entity that possesses a business-effective temporal range.
 * Entities implementing this interface should be able to provide a time range indicating when the entity
 * is considered valid from a business perspective.
 */
public interface BusinessTemporal {
    /**
     * Retrieves the business effective temporal range of the entity.
     *
     * @return the TemporalRange indicating when the entity is considered valid from a business perspective.
     */
    TemporalRange businessEffective();
}
