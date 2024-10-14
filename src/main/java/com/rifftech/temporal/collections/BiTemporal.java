package com.rifftech.temporal.collections;

/**
 * The BiTemporal interface combines both business effective and system effective time dimensions.
 * Classes implementing this interface should provide temporal data that is valid within both
 * business-effective and system-effective time ranges.
 */
public interface BiTemporal extends BusinessTemporal, SystemTemporal {
}
