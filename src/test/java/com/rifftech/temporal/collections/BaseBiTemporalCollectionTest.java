package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class BaseBiTemporalCollectionTest {

    /**
     * This class contains tests for method 'add' in the class BaseBiTemporalCollection.
     * The method add allows to add item to the collection.
     */

    @Test
    void testAdd_SingleItem() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange validTimeRange = TemporalRange.nowUntilMax();

        // act
        collection.add(item, validTimeRange);

        // assert
        assertEquals(1, collection.size());
    }

    @Test
    void testAdd_NullItem_ShouldThrowException() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        TemporalRange validTimeRange = TemporalRange.nowUntilMax();

        // act & assert
        assertThrows(IllegalArgumentException.class, () -> collection.add(null, validTimeRange));
    }

    @Test
    void testAdd_NullTemporalRange_ShouldThrowException() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";

        // act & assert
        assertThrows(IllegalArgumentException.class, () -> collection.add(item, null));
    }

    @Test
    void testAdd_MultipleItems() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item1 = "Hello";
        String item2 = "World";
        TemporalRange validTimeRange1 = TemporalRange.nowFor(Duration.ofDays(1));
        Instant now = Instant.now();
        TemporalRange validTimeRange2 = TemporalRange.fromTo(now.plus(Duration.ofDays(1)), now.plus(Duration.ofDays(2)));

        // act
        collection.add(item1, validTimeRange1);
        collection.add(item2, validTimeRange2);

        // assert
        assertEquals(2, collection.size());
    }

    @Test
    void testAdd_ItemAlreadyExists() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange validTimeRange = TemporalRange.nowUntilMax();

        // act
        collection.add(item, validTimeRange);
        collection.add(item, validTimeRange);

        // assert
        assertEquals(1, collection.size());
    }

    @Test
    void testAdd_ValidItemInEmptyCollection_HasItem() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange validTimeRange = TemporalRange.nowUntilMax();

        // act
        collection.add(item, validTimeRange);

        // assert
        assertTrue(collection.contains(item));
    }

    @Test
    void testAdd_OverlappingTimeRanges_ShouldThrowException() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item1 = "Hello";
        String item2 = "World";
        TemporalRange validTimeRange = TemporalRange.nowUntilMax();

        // act
        collection.add(item1, validTimeRange);

        // assert
        assertThrows(IllegalArgumentException.class, () -> collection.add(item2, validTimeRange));
    }

    @Test
    void testAdd_ItemWithTransactionTimeRangeInEmptyCollection_HasItem() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange validTimeRange = TemporalRange.nowUntilMax();
        TemporalRange transactionTimeRange = TemporalRange.nowFor(Duration.ofHours(1));

        // act
        collection.add(item, validTimeRange, transactionTimeRange);

        // assert
        assertTrue(collection.contains(item));
    }

    @Test
    void delete_shouldRemoveItemFromCollection_whenItemPresentInGivenRange() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();
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
    void delete_shouldNotRemoveItemFromCollection_whenItemNotPresentInGivenRange() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();
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
    void delete_shouldNotRemoveItemFromCollection_whenItemNotPresent() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();
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
        BaseBiTemporalCollection<String> collection = createCollection();
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
        BaseBiTemporalCollection<String> collection = createCollection();
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
    void testDelete_ItemPresentInGivenRange_ShouldRemoveItem() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // act
        collection.delete(item, range);

        // assert
        Optional<String> retrievedItem = collection.getAsOf(Instant.now());
        assertFalse(retrievedItem.isPresent());
    }

    @Test
    void testDelete_ItemNotPresentInGivenRange_ShouldNotRemoveItem() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange nonOverlappingRange = new TemporalRange(range.end(), range.end().plus(Duration.ofDays(1)));
        collection.add(item, range);

        // act
        collection.delete(item, nonOverlappingRange);

        // assert
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);
        assertEquals(1, history.size());
    }

    @Test
    void testDelete_TransactionTimeRangeOverlaps_ShouldRemoveItem() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange validRange = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange transactionRange = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, validRange, transactionRange);

        // act
        collection.delete(item, validRange, transactionRange);

        // assert
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);
        assertEquals(0, history.size());
    }

    @Test
    void testDelete_TransactionTimeRangeDoesNotOverlap_ShouldNotRemoveItem() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange validRange = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange initialTransactionRange = TemporalRange.nowFor(Duration.ofDays(1));
        Instant now = Instant.now();
        TemporalRange newTransactionRange = TemporalRange.fromTo(now.plus(Duration.ofDays(1)), now.plus(Duration.ofDays(2)));
        collection.add(item, validRange, initialTransactionRange);

        // act
        collection.delete(item, validRange, newTransactionRange);

        // assert
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);
        assertEquals(1, history.size());
    }

    @Test
    void testDelete_ItemNotPresentInCollection_ShouldNotRemoveItem() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        String notExistentItem = "World";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add(item, range);

        // act
        collection.delete(notExistentItem, range);

        // assert
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);
        assertEquals(1, history.size());
    }

    @Test
    void deleteAll_shouldDoNothing_whenItemNotPresent() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();
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
    void testDelete_ItemNotPresentInCollection_ShouldNotRemoveAnyItems() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        String notExistentItem = "World";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange transactionTimeRange = TemporalRange.nowFor(Duration.ofHours(1));
        collection.add(item, range, transactionTimeRange);
        // act
        collection.delete(notExistentItem, range, transactionTimeRange);
        // assert
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);
        assertEquals(1, history.size());
    }

    @Test
    void testDelete_ItemNotPresentInGivenRange_ShouldNotRemoveAnyItems() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange transactionTimeRange = TemporalRange.nowFor(Duration.ofHours(1));
        TemporalRange nonOverlappingRange = new TemporalRange(range.end(), range.end().plus(Duration.ofDays(1)));
        collection.add(item, range, transactionTimeRange);
        // act
        collection.delete(item, nonOverlappingRange, transactionTimeRange);
        // assert
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);
        assertEquals(1, history.size());
    }

    @Test
    void testDelete2_ItemPresentInGivenRange_ShouldRemoveItem() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        TemporalRange transactionTimeRange = TemporalRange.nowFor(Duration.ofHours(1));
        collection.add(item, range, transactionTimeRange);
        // act
        collection.delete(item, range, transactionTimeRange);
        // assert
        Collection<HistoricalRecord<String>> history = collection.getHistory(item);
        assertTrue(history.isEmpty());
    }

    @Test
    void testGetTransactionAsOf_ShouldReturnItemsInTransactionTime() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item1 = "Hello";
        String item2 = "World";
        TemporalRange validTimeRange = TemporalRange.nowUntilMax();
        TemporalRange transactionTimeRange1 = TemporalRange.nowFor(Duration.ofHours(1));
        TemporalRange transactionTimeRange2 = TemporalRange.fromTo(transactionTimeRange1.end(), transactionTimeRange1.end().plus(Duration.ofHours(2)));
        collection.add(item1, validTimeRange, transactionTimeRange1);
        collection.add(item2, validTimeRange, transactionTimeRange2);

        // act
        Collection<String> items = collection.getTransactionAsOf(Instant.now().plus(Duration.ofMinutes(30)));

        // assert
        assertEquals(1, items.size());
        assertTrue(items.contains(item1));
    }

    @Test
    void testGetTransactionAsOf_ShouldReturnEmptyCollectionWhenNoItemsInTransactionTime() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        String item = "Hello";
        TemporalRange validTimeRange = TemporalRange.nowUntilMax();
        TemporalRange transactionTimeRange = TemporalRange.nowFor(Duration.ofHours(1));
        collection.add(item, validTimeRange, transactionTimeRange);

        // act
        Collection<String> items = collection.getTransactionAsOf(Instant.now().plus(Duration.ofHours(2)));

        // assert
        assertTrue(items.isEmpty());
    }

    @Test
    void testGetTransactionAsOf_NullTransactionTime_ShouldThrowException() {
        // arrange
        BaseBiTemporalCollection<String> collection = createCollection();

        // act & assert
        assertThrows(IllegalArgumentException.class, () -> collection.getTransactionAsOf(null));
    }

    @Test
    void isEmpty_shouldReturnTrue_whenCollectionIsEmpty() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();

        // Act
        boolean result = collection.isEmpty();

        // Assert
        assertTrue(result, "Collection should be empty.");
    }

    @Test
    void isEmpty_shouldReturnFalse_whenCollectionIsNotEmpty() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add("Hello", range);

        // Act
        boolean result = collection.isEmpty();

        // Assert
        assertFalse(result, "Collection should not be empty.");
    }

    @Test
    void getAsOf_ShouldReturnEmptyOptional_whenTransactionRangeDoesNotContainInstant() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add("Hello", range, range);

        // Act
        Optional<String> optional = collection.getAsOf(range.start(), Instant.now().minusSeconds(10));

        assertThat(optional).isEmpty();
    }

    @Test
    void getAsOf_ShouldReturnNotEmptyOptional_whenTransactionRangeContainsInstant() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add("Hello", range, range);

        // Act
        Optional<String> optional = collection.getAsOf(range.start(), range.start());

        assertThat(optional).isPresent();
    }

    @Test
    void getAsOf_ShouldReturnEmptyOptional_whenValidRangeDoesNotContainInstant() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add("Hello", range, range);

        // Act
        Optional<String> optional = collection.getAsOf(range.start().minus(Duration.ofSeconds(1)), range.start());

        assertThat(optional).isEmpty();
    }

    @Test
    void getInRange_ShouldReturnEmptyCollection_whenTransactionRangeDoesNotContainInstant() {
        // Arrange
        BaseBiTemporalCollection<String> collection = createCollection();
        TemporalRange range = TemporalRange.nowFor(Duration.ofDays(1));
        collection.add("Hello", range, range);

        // Act
        Collection<String> optional = collection.getInRange(range, range.minus(Duration.ofDays(1)));

        assertThat(optional).isEmpty();
    }

    @Test
    public void testIterator_whenCollectionIsEmpty() {
        BaseBiTemporalCollection<String> collection = createCollection();
        assertFalse(collection.iterator().hasNext());
    }

    @Test
    public void testIterator_whenCollectionHasOneElement() {
        BaseBiTemporalCollection<String> collection = createCollection();
        TemporalRange validTimeRange = TemporalRange.nowUntilMax();
        collection.add("Test item", validTimeRange);
        Iterator<String> iterator = collection.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("Test item", iterator.next());
    }

    @Test
    public void testIterator_whenCollectionHasMultipleElements() {
        BaseBiTemporalCollection<String> collection = createCollection();
        TemporalRange validTimeRange1 = TemporalRange.nowUntil(Instant.now().plusSeconds(1));
        TemporalRange validTimeRange2 = validTimeRange1.plus(Duration.ofSeconds(1));
        TemporalRange validTimeRange3 = validTimeRange2.plus(Duration.ofSeconds(1));
        collection.add("Test 1", validTimeRange1);
        collection.add("Test 2", validTimeRange2);
        collection.add("Test 3", validTimeRange3);
        Iterator<String> iterator = collection.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("Test 1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Test 2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Test 3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    protected abstract BaseBiTemporalCollection<String> createCollection();
}
