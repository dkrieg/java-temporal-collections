package com.rifftech.temporal.collections;

/**
 * A NavigableBiTemporalPropertyCollection is a specialized collection that associates items of type {@code T}
 * with specific valid and transaction temporal ranges. It allows querying of items based on both
 * their valid and transaction times.
 *
 * @param <T> the type of elements in this bi-temporal collection
 */
public class NavigableBiTemporalPropertyCollection<T> extends BaseBiTemporalCollection<T> implements BiTemporalPropertyCollection<T> {

}