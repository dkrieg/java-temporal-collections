package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class BaseTemporalCollectionTest {


    @Test
    void getInRange_shouldReturnItemsInRange_whenItemsExistInValidRange() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item1 = "Hello";
        String item2 = "World";
        TemporalRange range1 = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange range2 = new TemporalRange(range1.end(), range1.end().plus(Duration.ofDays(1)));
        collection.add(item1, range1);
        collection.add(item2, range2);

        // Act
        Collection<String> retrievedItems = collection.getInRange(TemporalRange.nowFor(Duration.ofDays(3)));

        // Assert
        assertEquals(2, retrievedItems.size());
        assertTrue(retrievedItems.contains(item1));
        assertTrue(retrievedItems.contains(item2));
    }


    @Test
    void getInRange_shouldNotReturnItemsOutOfRange_whenItemsExistOutOfValidRange() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // Act
        Collection<String> retrievedItems = collection.getInRange(new TemporalRange(range.start().plus(Duration.ofDays(1)), range.end().plus(Duration.ofDays(1))));

        // Assert
        assertTrue(retrievedItems.isEmpty());
    }

    @Test
    void getHistory_shouldReturnEmpty_whenNoHistoryExistsForItem() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        String notItem = "World";
        collection.add(item, TemporalRange.nowFor(Duration.ofDays(1)));

        // Act
        Collection<HistoricalRecord<String>> history = collection.getHistory(notItem);

        // Assert
        assertTrue(history.isEmpty(), "History should be empty when no history exists for item");
    }

    @Test
    void getHistory_shouldReturnHistory_whenHistoryExistsForItem() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // Act
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);

        // Assert
        assertEquals(1, history.size(), "History size should be 1 when there's a single history for item");
        assertEquals(item, history.iterator().next().item());
    }

    @Test
    void delete_shouldRemoveItemFromCollection_whenItemPresentInGivenRange() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // Act
        collection.delete(item, range);

        // Assert
        Optional<String> retrievedItem = collection.getAsOf(Instant.now());
        assertFalse(retrievedItem.isPresent(), "The item should be removed from the collection.");
    }

    @Test
    void testAdd_ItemPresentInGivenRange() {
        // arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // assert
        Optional<String> retrievedItem = collection.getAsOf(range.start());
        assertTrue(retrievedItem.isPresent());
    }

    @Test
    void testAdd_ItemNotPresentInGivenRange() {
        // arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // assert
        Optional<String> retrievedItem = collection.getAsOf(range.start().plus(Duration.ofDays(1)));
        assertTrue(retrievedItem.isEmpty());
    }

    @Test
    void delete_shouldNotRemoveItemFromCollection_whenItemNotPresentInGivenRange() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange nonOverlappingRange = new TemporalRange(range.end(), range.end().plus(Duration.ofDays(1)));
        collection.add(item, range);

        // Act
        collection.delete(item, nonOverlappingRange);

        // Assert
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);
        assertEquals(1, history.size(), "The item should not be removed from the collection if it's not present in the given range.");
    }

    @Test
    void size_shouldReturnZero_whenCollectionIsEmpty() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        // Act
        int size = collection.size();
        // Assert
        assertEquals(0, size);
    }

    @Test
    void size_shouldReturnNumberOfItems_whenCollectionIsNotEmpty() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add("Hello", range);
        collection.add("World", range.plus(Duration.ofDays(1)));
        // Act
        int size = collection.size();
        // Assert
        assertEquals(2, size);
    }

    @Test
    void delete_shouldNotRemoveItemFromCollection_whenItemNotPresent() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        String notItem = "World";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // Act
        collection.delete(notItem, range);

        // Assert
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);
        assertEquals(1, history.size(), "The item should not be removed from the collection if it's not present.");
    }

    @Test
    void deleteAll_shouldRemoveAllInstancesOfItem_whenMultipleInstancesPresent() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range1 = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange range2 = new TemporalRange(range1.end(), range1.end().plus(Duration.ofDays(1)));
        collection.add(item, range1);
        collection.add(item, range2);

        // Act
        collection.deleteAll(item);

        // Assert
        Collection<String> retrievedItems = collection.getInRange(range1);
        assertTrue(retrievedItems.isEmpty(), "All instances of the item should be removed from the collection.");
    }

    @Test
    void deleteAll_shouldNotRemoveOtherItems_whenItemPresent() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item1 = "Hello";
        String item2 = "World";
        TemporalRange range1 = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange range2 = new TemporalRange(range1.end(), range1.end().plus(Duration.ofDays(1)));
        collection.add(item1, range1);
        collection.add(item2, range2);

        // Act
        collection.deleteAll(item1);

        // Assert
        Collection<String> retrievedItemsInRange2 = collection.getInRange(range2);
        assertTrue(retrievedItemsInRange2.contains(item2), "The other item should not be removed from the collection.");
        Collection<String> retrievedItemsInRange1 = collection.getInRange(range1);
        assertFalse(retrievedItemsInRange1.contains(item1), "The specific item should be removed from the collection.");
    }

    @Test
    void isEmpty_shouldReturnTrue_whenCollectionIsEmpty() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();

        // Act
        boolean result = collection.isEmpty();

        // Assert
        assertTrue(result, "Collection should be empty.");
    }

    @Test
    void isEmpty_shouldReturnFalse_whenCollectionIsNotEmpty() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add("Hello", range);

        // Act
        boolean result = collection.isEmpty();

        // Assert
        assertFalse(result, "Collection should not be empty.");
    }

    @Test
    void deleteAll_shouldDoNothing_whenItemNotPresent() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item1 = "Hello";
        String item2 = "World";
        TemporalRange range1 = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item1, range1);

        // Act
        collection.deleteAll(item2);

        // Assert
        Collection<String> retrievedItems = collection.getInRange(range1);
        assertTrue(retrievedItems.contains(item1), "The original items should not be deleted if the item to be deleted is not present in the collection.");
    }

    @Test
    void contains_shouldReturnTrue_whenItemPresentInCollection() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // Act
        boolean result = collection.contains(item);

        // Assert
        assertTrue(result, "Collection should contain the item.");
    }

    @Test
    void contains_shouldReturnFalse_whenItemNotPresentInCollection() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        String notItem = "World";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // Act
        boolean result = collection.contains(notItem);

        // Assert
        assertFalse(result, "Collection should not contain the item.");
    }

    @Test
    void iterator_shouldReturnValidData_whenCollectionIsNotEmpty() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        String item = "Hello";
        collection.add(item, range);

        // Act
        Iterator<String> iterator = collection.iterator();

        // Assert
        assertTrue(iterator.hasNext());
        assertEquals(item, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void iterator_shouldReturnEmptyIterator_whenCollectionIsEmpty() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();

        // Act
        Iterator<String> iterator = collection.iterator();

        // Assert
        assertFalse(iterator.hasNext());
    }

    @Test
    void validateTemporalRange_shouldThrowException_whenRangeIsNull() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        TemporalRange range = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> collection.validateTemporalRange(range), "TemporalRange cannot be null.");
    }

    @Test
    void validateTemporalRange_shouldNotThrowException_whenRangeIsValid() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));

        // Act & Assert
        assertDoesNotThrow(() -> collection.validateTemporalRange(range));
    }

    @Test
    void checkOverlap_shouldNotThrowException_whenNoOverlap() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        TemporalRange range1 = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange range2 = new TemporalRange(range1.end(), range1.end().plus(Duration.ofDays(1)));
        String item = "Hello";
        collection.add(item, range1);

        // Act & Assert
        assertDoesNotThrow(() -> collection.checkOverlap(item, range2));
    }

    @Test
    void validateItem_shouldThrowException_whenItemIsNull() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> collection.validateItem(item), "Item cannot be null.");
    }

    @Test
    void validateItem_shouldNotThrowException_whenItemIsValid() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";

        // Act & Assert
        assertDoesNotThrow(() -> collection.validateItem(item));
    }

    @Test
    void checkOverlap_shouldThrowException_whenOverlapExists() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        TemporalRange range1 = TemporalRange.nowFor(Duration.ofDays(2));
        TemporalRange range2 = TemporalRange.nowFor(Duration.ofDays(1));
        String item = "Hello";
        collection.add(item, range1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> collection.checkOverlap(item, range2), "Overlap detected with existing range: " + range1);
    }

    @Test
    void validateInstant_shouldThrowException_whenInstantIsNull() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        Instant validTime = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> collection.validateInstant(validTime), "Instant cannot be null.");
    }

    @Test
    void validateInstant_shouldNotThrowException_whenInstantIsValid() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        Instant validTime = Instant.now();

        // Act & Assert
        assertDoesNotThrow(() -> collection.validateInstant(validTime));
    }

    @Test
    void getAsOfNow_shouldReturnEmptyOptional_whenItemListIsEmpty() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();

        // Act
        Optional<String> result = collection.getAsOfNow();

        // Assert
        assertFalse(result.isPresent(), "Optional should be empty when collection is empty.");
    }

    @Test
    void getAsOfNow_shouldReturnItem_whenSingleItemInCollection() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // Act
        Optional<String> result = collection.getAsOfNow();

        // Assert
        assertTrue(result.isPresent(), "Optional should not be empty when item is present.");
        assertEquals(item, result.get(), "Returned item should match added item.");
    }

    @Test
    void getAsOfNow_shouldReturnLatestItem_whenMultipleItemsInCollection() {
        // Arrange
        BaseTemporalCollection<String> collection = createCollection();
        String item1 = "Hello";
        String item2 = "World";
        TemporalRange range1 = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange range2 = new TemporalRange(range1.end(), range1.end().plus(Duration.ofDays(1)));
        collection.add(item1, range1);
        collection.add(item2, range2);

        // Act
        Optional<String> result = collection.getAsOfNow();

        // Assert
        assertTrue(result.isPresent(), "Optional should not be empty when items are present.");
        assertEquals(item1, result.get(), "Returned item should match the item in range1.");
    }

    protected abstract BaseTemporalCollection<String> createCollection();
}

