package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NavigableBusinessTemporalObjectCollectionTest {

    @Test
    public void shouldAddBusinessTemporalItem() {
        // create mock for BusinessTemporal interface
        BusinessTemporal mockBusinessTemporal = Mockito.mock(BusinessTemporal.class);

        // simulate the behaviour when `businessEffective` method of `BusinessTemporal` is called
        Mockito.when(mockBusinessTemporal.businessEffective()).thenReturn(TemporalRange.nowUntilMax());

        // create object of NavigableBusinessTemporalObjectCollection
        NavigableBusinessTemporalObjectCollection<BusinessTemporal> collection = new NavigableBusinessTemporalObjectCollection<>();

        //call the method to be tested
        collection.add(mockBusinessTemporal);

        //check if the mock method is called correctly
        Mockito.verify(mockBusinessTemporal, Mockito.times(1)).businessEffective();
    }

    @Test
    public void shouldDeleteBusinessTemporalItem() {
        // create mock for BusinessTemporal interface
        BusinessTemporal mockBusinessTemporal = Mockito.mock(BusinessTemporal.class);

        // simulate the behaviour when `businessEffective` method of `BusinessTemporal` is called
        Mockito.when(mockBusinessTemporal.businessEffective()).thenReturn(TemporalRange.nowUntilMax());

        // create object of NavigableBusinessTemporalObjectCollection
        NavigableBusinessTemporalObjectCollection<BusinessTemporal> collection = new NavigableBusinessTemporalObjectCollection<>();

        //call the method to be tested
        collection.delete(mockBusinessTemporal);

        //check if the mock method is called correctly
        Mockito.verify(mockBusinessTemporal, Mockito.times(1)).businessEffective();
    }
}