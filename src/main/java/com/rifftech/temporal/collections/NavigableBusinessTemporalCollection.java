package com.rifftech.temporal.collections;

public class NavigableBusinessTemporalCollection<T extends BusinessTemporal> extends AbstractNavigableTemporalCollection<T> {
    @Override
    protected TemporalRange validTimeRange(T item) {
        return item.businessEffective();
    }
}
