package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NavigableBiTemporalObjectCollectionTest {

    /**
     * NavigableBiTemporalObjectCollection is a type of collection that supports
     * storing and deleting BiTemporal objects. The add method is used to add a
     * new BiTemporal object into the collection.
     * <p>
     * The test method below will test the add method of the class.
     */

    @Test
    void testAdd() {
        NavigableBiTemporalObjectCollection<BiTemporal> collection = new NavigableBiTemporalObjectCollection<>();

        BiTemporal mockBiTemporal = mock(BiTemporal.class);
        when(mockBiTemporal.businessEffective()).thenReturn(null);
        when(mockBiTemporal.systemEffective()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> collection.add(mockBiTemporal));

        TemporalRange mockTemporalRange = mock(TemporalRange.class);
        when(mockBiTemporal.businessEffective()).thenReturn(mockTemporalRange);
        when(mockBiTemporal.systemEffective()).thenReturn(mockTemporalRange);

        // Should not throw any exceptions now since item is properly mocked
        collection.add(mockBiTemporal);
    }

    // Other tests...

    /**
     * The delete method in the NavigableBiTemporalObjectCollection class removes
     * a BiTemporal object from the collection.
     * <p>
     * The test method below will test the delete method of the class.
     */
    @Test
    void testDeleteWhenItemExists() {
        NavigableBiTemporalObjectCollection<BiTemporal> collection = new NavigableBiTemporalObjectCollection<>();

        BiTemporal mockBiTemporal = mock(BiTemporal.class);
        TemporalRange mockTemporalRange = mock(TemporalRange.class);
        when(mockBiTemporal.businessEffective()).thenReturn(mockTemporalRange);
        when(mockBiTemporal.systemEffective()).thenReturn(mockTemporalRange);

        // Adding item first to the collection
        collection.add(mockBiTemporal);

        // Should not throw any exceptions now since item is properly mocked and added to collection
        collection.delete(mockBiTemporal);
    }

    @Test
    void testDeleteWhenItemDoesNotExist() {
        NavigableBiTemporalObjectCollection<BiTemporal> collection = new NavigableBiTemporalObjectCollection<>();

        BiTemporal mockBiTemporal = mock(BiTemporal.class);
        TemporalRange mockTemporalRange = mock(TemporalRange.class);
        when(mockBiTemporal.businessEffective()).thenReturn(mockTemporalRange);
        when(mockBiTemporal.systemEffective()).thenReturn(mockTemporalRange);

        assertDoesNotThrow(() -> collection.delete(mockBiTemporal));
    }
}
