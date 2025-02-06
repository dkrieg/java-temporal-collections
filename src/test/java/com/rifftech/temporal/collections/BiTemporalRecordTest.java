package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class represents test cases for the BiTemporalRecord class.
 * The compareTo method of this class is tested in various scenarios.
 */
public class BiTemporalRecordTest {

    @Test
    public void when_CompareTo_Then_ResultBasedOnValidRange() {
        // Creating a mock TemporalRange
        TemporalRange validRange1 = Mockito.mock(TemporalRange.class);
        TemporalRange validRange2 = Mockito.mock(TemporalRange.class);
        TemporalRange transactionRange = Mockito.mock(TemporalRange.class);

        // Mocking compareTo method
        Mockito.when(validRange1.compareTo(validRange2)).thenReturn(1);

        // Creating BiTemporalRecord objects
        BiTemporalRecord<String> record1 = new BiTemporalRecord<>(validRange1, transactionRange, "Value1");
        BiTemporalRecord<String> record2 = new BiTemporalRecord<>(validRange2, transactionRange, "Value2");

        // Assertion
        assertEquals(1, record1.compareTo(record2));
    }

    @Test
    public void when_CompareTo_Then_ResultBasedOnTransactionRange() {
        TemporalRange range1 = Mockito.mock(TemporalRange.class);
        TemporalRange range2 = Mockito.mock(TemporalRange.class);
        TemporalRange range3 = Mockito.mock(TemporalRange.class);

        Mockito.when(range1.compareTo(range2)).thenReturn(0); // same valid range of records
        Mockito.when(range1.compareTo(range3)).thenReturn(1); // different transaction range of records

        BiTemporalRecord<String> record1 = new BiTemporalRecord<>(range1, range2, "Value1");
        BiTemporalRecord<String> record2 = new BiTemporalRecord<>(range1, range3, "Value2");

        assertEquals(1, record1.compareTo(record2));
    }

    @Test
    public void when_CompareToGivenEqualRanges_Then_ZeroReturned() {
        TemporalRange range1 = Mockito.mock(TemporalRange.class);

        TemporalRecord<String> record1 = new TemporalRecord<>(range1, "Value1");
        TemporalRecord<String> record2 = new TemporalRecord<>(range1, "Value2");

        assertEquals(0, record1.compareTo(record2));
    }

    @Test
    public void when_CompareToGivenDifferentRanges_Then_NonZeroReturned() {
        TemporalRange range1 = Mockito.mock(TemporalRange.class);
        TemporalRange range2 = Mockito.mock(TemporalRange.class);

        Mockito.when(range1.compareTo(range2)).thenReturn(1);

        TemporalRecord<String> record1 = new TemporalRecord<>(range1, "Value1");
        TemporalRecord<String> record2 = new TemporalRecord<>(range2, "Value2");

        assertEquals(1, record1.compareTo(record2));
    }
}
