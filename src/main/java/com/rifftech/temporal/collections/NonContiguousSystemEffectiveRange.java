package com.rifftech.temporal.collections;

/**
 * Exception thrown when a set of system effective ranges is identified as non-contiguous.
 * <p>
 * A system effective range is considered non-contiguous when there are gaps or overlaps
 * in the effective time ranges that represent the system's temporal states.
 * </p>
 * <p>
 * This exception is commonly used in scenarios where bi-temporal records need to maintain
 * a strictly contiguous sequence of system effective ranges to ensure temporal integrity.
 * </p>
 */
public class NonContiguousSystemEffectiveRange extends RuntimeException {
    public NonContiguousSystemEffectiveRange() {
        super();
    }
}
