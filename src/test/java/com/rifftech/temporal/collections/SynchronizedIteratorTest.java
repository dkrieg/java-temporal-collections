package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SynchronizedIteratorTest {

    @Test
    public void testHasNextWhenElementsExistInIterator() {
        Iterator<Integer> iterator = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)).iterator();
        SynchronizedIterator<Integer> synchronizedIterator = new SynchronizedIterator<>(iterator);

        assertTrue(synchronizedIterator.hasNext());
    }

    @Test
    public void testHasNextWhenNoElementsExistInIterator() {
        Iterator<Integer> iterator = Collections.emptyIterator();
        SynchronizedIterator<Integer> synchronizedIterator = new SynchronizedIterator<>(iterator);

        assertFalse(synchronizedIterator.hasNext());
    }

    @Test
    public void testNextWhenElementsExistInIterator() {
        Integer expected = 1;
        Iterator<Integer> iterator = new HashSet<>(List.of(expected)).iterator();
        SynchronizedIterator<Integer> synchronizedIterator = new SynchronizedIterator<>(iterator);

        Integer actual = synchronizedIterator.next();

        assertTrue(expected.equals(actual));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNextWhenNoElementsExistInIterator() {
        Iterator<Integer> iterator = mock(Iterator.class);
        when(iterator.next()).thenThrow(NoSuchElementException.class);
        SynchronizedIterator<Integer> synchronizedIterator = new SynchronizedIterator<>(iterator);

        assertThrows(NoSuchElementException.class, synchronizedIterator::next);
    }

    @Test
    public void testRemoveWhenElementsExistInIterator() {
        Iterator<Integer> iterator = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)).iterator();
        SynchronizedIterator<Integer> synchronizedIterator = new SynchronizedIterator<>(iterator);

        synchronizedIterator.next();
        synchronizedIterator.remove();

        assertTrue(synchronizedIterator.hasNext());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemoveWhenNoElementsExistInIterator() {
        Iterator<Integer> iterator = mock(Iterator.class);
        doThrow(IllegalStateException.class).when(iterator).remove();
        SynchronizedIterator<Integer> synchronizedIterator = new SynchronizedIterator<>(iterator);

        assertThrows(IllegalStateException.class, synchronizedIterator::remove);
    }
}