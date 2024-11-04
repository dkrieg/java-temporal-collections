package com.rifftech.temporal.collections;

public class NavigableSystemTemporalCollection<T extends SystemTemporal> extends AbstractNavigableTemporalCollection<T> {
    @Override
    protected TemporalRange validTimeRange(T item) {
        return item.systemEffective();
    }
}
