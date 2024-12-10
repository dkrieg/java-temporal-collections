package com.rifftech.temporal.collections;

class BusinessTemporalCollectionTest extends AbstractTemporalCollectionTest<BusinessTemporalValue<Integer>> {

    @Override
    protected AbstractTemporalCollection<Integer, BusinessTemporalValue<Integer>> createInstance() {
        return new BusinessTemporalCollection<>();
    }
}