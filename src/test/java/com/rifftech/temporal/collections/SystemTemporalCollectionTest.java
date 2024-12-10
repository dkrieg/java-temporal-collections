package com.rifftech.temporal.collections;

class SystemTemporalCollectionTest extends AbstractTemporalCollectionTest<SystemTemporalValue<Integer>> {

    @Override
    protected AbstractTemporalCollection<Integer, SystemTemporalValue<Integer>> createInstance() {
        return new SystemTemporalCollection<>();
    }
}