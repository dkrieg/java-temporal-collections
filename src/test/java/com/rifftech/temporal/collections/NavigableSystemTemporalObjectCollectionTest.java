package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NavigableSystemTemporalObjectCollectionTest {

    @Test
    public void testAdd() {
        // Mock the SystemTemporal object
        SystemTemporal mockedTemporal = Mockito.mock(SystemTemporal.class);
        NavigableSystemTemporalObjectCollection<SystemTemporal> collection = new NavigableSystemTemporalObjectCollection<>();

        // Define the behavior of mocked object
        Mockito.when(mockedTemporal.systemEffective()).thenReturn(TemporalRange.nowUntilMax());

        // Adding item
        collection.add(mockedTemporal);

        // Verify if "systemEffective()" was called once
        Mockito.verify(mockedTemporal, Mockito.times(1)).systemEffective();
    }

    @Test
    public void testDelete() {
        // Mock the SystemTemporal object
        SystemTemporal mockedTemporal = Mockito.mock(SystemTemporal.class);
        NavigableSystemTemporalObjectCollection<SystemTemporal> collection = new NavigableSystemTemporalObjectCollection<>();

        // Define the behavior of mocked object
        Mockito.when(mockedTemporal.systemEffective()).thenReturn(TemporalRange.nowUntilMax());

        // Adding item
        collection.delete(mockedTemporal);

        // Verify if "systemEffective()" was called once
        Mockito.verify(mockedTemporal, Mockito.times(1)).systemEffective();
    }
}