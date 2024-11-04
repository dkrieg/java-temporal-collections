package com.rifftech.temporal.collections;

public class SystemTemporalHashMap<K, V extends SystemTemporal> extends AbstractTemporalMap<K, V, NavigableSystemTemporalCollection<V>> {
    @Override
    protected NavigableSystemTemporalCollection<V> newNavigableTemporalCollection() {
        return new NavigableSystemTemporalCollection<>();
    }
}
