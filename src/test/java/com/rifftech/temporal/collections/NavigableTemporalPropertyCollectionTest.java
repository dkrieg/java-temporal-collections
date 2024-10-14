package com.rifftech.temporal.collections;

class NavigableTemporalPropertyCollectionTest extends BaseTemporalCollectionTest {

    @Override
    protected BaseTemporalCollection<String> createCollection() {
        return new NavigableTemporalPropertyCollection<>();
    }
}