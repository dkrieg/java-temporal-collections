package com.rifftech.temporal.collections;

public class BusinessTemporalHashMap<K, V extends BusinessTemporal> extends AbstractTemporalMap<K, V, NavigableBusinessTemporalCollection<V>> {
    @Override
    protected NavigableBusinessTemporalCollection<V> newNavigableTemporalCollection() {
        return new NavigableBusinessTemporalCollection<>();
    }
}
