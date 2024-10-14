package com.rifftech.temporal.collections;

class NavigableBiTemporalPropertyCollectionTest extends BaseBiTemporalCollectionTest {

    @Override
    protected BaseBiTemporalCollection<String> createCollection() {
        return new NavigableBiTemporalPropertyCollection<>();
    }
}