package com.rifftech.temporal.collections;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Iterator;

import static java.util.Objects.isNull;

/**
 * Represents a range of time defined by a start and an end {@link Instant}.
 * This record implements {@link Comparable<TemporalRange>} for natural ordering.
 *
 * @param start the starting {@link Instant} of the range, must be less than {@code end}
 * @param end   the ending {@link Instant} of the range, must be greater than {@code start}
 */
public record TemporalRange(Instant start, Instant end) implements Comparable<TemporalRange> {
    public static final Instant MIN = Instant.parse("-9999-01-01T00:00:00Z");
    public static final Instant MAX = Instant.parse("9999-12-31T23:59:59Z");

    /**
     * A constant TemporalRange representing an infinite duration starting from the earliest possible instant
     * to the latest possible instant. This can be used to denote a time range that effectively has no bounds
     * or limitations and is intended to be used in contexts where an unbounded temporal range is required.
     */
    public static final TemporalRange FOREVER = new TemporalRange(MIN, MAX);

    private static void validateInstants(Instant start, Instant end) {
        if (start == null || end == null || !start.isBefore(end)) {
            throw new IllegalArgumentException("Start must be before end and neither can be null.");
        }
    }

    public static TemporalRange parse(String text) {
        String[] split = text.split(" to ");
        if (split.length != 2) {
            throw new IllegalArgumentException("%s must be formatted as start,end like \"2024-10-30T13:00:00Z to 2024-10-30T14:00:00Z\"".formatted(text));
        }
        return new TemporalRange(Instant.parse(split[0]), Instant.parse(split[1]));
    }

    /**
     * Constructs a TemporalRange object and validates that the start and end instants are not null
     * and that the start is before the end.
     *
     * @param start the starting point in time of the range
     * @param end   the ending point in time of the range
     */
    public TemporalRange {
        validateInstants(start, end);
    }

    /**
     * Creates a TemporalRange starting from the current instant until a specified end instant.
     *
     * @param end the ending point in time of the range
     * @return a TemporalRange object from the current instant to the specified end instant
     */
    public static TemporalRange nowUntil(Instant end) {
        return new TemporalRange(Instant.now(), end);
    }

    public static TemporalRange nowUntil(Instant end, ChronoUnit precision) {
        return new TemporalRange(Instant.now().truncatedTo(precision), end.truncatedTo(precision));
    }


    /**
     *
     */
    public static TemporalRange nowUntilMax() {
        return new TemporalRange(Instant.now(), MAX);
    }

    /**
     * Creates a {@code TemporalRange} that starts from the current time and extends to the maximum possible time,
     * both truncated to the specified precision.
     *
     * @param precision the {@code ChronoUnit} indicating the desired precision for truncating the start and end instants.
     * @return a {@code TemporalRange} starting from the current time to the maximum time, truncated to the given precision.
     */
    public static TemporalRange nowUntilMax(ChronoUnit precision) {
        validateNotNull(precision, "The precision cannot be null.");
        return new TemporalRange(Instant.now().truncatedTo(precision), MAX.truncatedTo(precision));
    }


    /**
     * Creates a {@code TemporalRange} that starts at the current instant and extends for the specified duration.
     *
     * @param duration the duration for which the temporal range should extend from the current instant.
     * @return a {@code TemporalRange} starting from the current instant and extending for the given duration.
     */
    public static TemporalRange nowFor(Duration duration) {
        validateNotNull(duration, "The duration cannot be null.");
        validateDuration(duration);
        Instant now = Instant.now();
        return new TemporalRange(now, now.plus(duration));
    }

    /**
     * Creates a TemporalRange starting from the current instant and lasting for a specified duration,
     * truncated to a given precision.
     *
     * @param duration the duration for which the temporal range should last; must not be null.
     * @param precision the ChronoUnit to which the start and end instants should be truncated; must not be null.
     * @return a TemporalRange object representing the time from now until the specified duration has passed,
     *         truncated to the given precision.
     */
    public static TemporalRange nowFor(Duration duration, ChronoUnit precision) {
        validateNotNull(duration, "The duration cannot be null.");
        validateNotNull(precision, "The precision cannot be null.");
        validateDuration(duration);
        Instant now = Instant.now().truncatedTo(precision);
        return new TemporalRange(now, now.plus(duration));
    }


    /**
     * Creates a TemporalRange object from the given start and end instants.
     *
     * @param start the starting point in time of the range; must not be null
     * @param end the ending point in time of the range; must not be null
     * @return a TemporalRange object from the given start instant to the given end instant
     */
    public static TemporalRange fromTo(Instant start, Instant end) {
        validateNotNull(start, "The start cannot be null.");
        validateNotNull(end, "The end cannot be null.");
        return new TemporalRange(start, end);
    }

    /**
     * Creates a TemporalRange object from the given start and end instants, truncated to the specified precision.
     *
     * @param start the starting point in time of the range; must not be null.
     * @param end the ending point in time of the range; must not be null.
     * @param precision the ChronoUnit to which the start and end instants should be truncated; must not be null.
     * @return a TemporalRange object from the given start instant to the given end instant, truncated to the given precision.
     */
    public static TemporalRange fromTo(Instant start, Instant end, ChronoUnit precision) {
        validateNotNull(start, "The start cannot be null.");
        validateNotNull(end, "The end cannot be null.");
        validateNotNull(precision, "The precision cannot be null.");
        return new TemporalRange(start.truncatedTo(precision), end.truncatedTo(precision));
    }


    /**
     * Creates a TemporalRange starting from the specified start instant until the current instant.
     *
     * @param start the starting point in time of the range; must not be null.
     * @return a TemporalRange object from the specified start instant to the current instant.
     */
    public static TemporalRange fromToNow(Instant start) {
        validateNotNull(start, "The start cannot be null.");
        return new TemporalRange(start, Instant.now());
    }

    /**
     * Creates a TemporalRange starting from the specified start instant until the current instant,
     * truncated to the specified precision.
     *
     * @param start the starting point in time of the range; must not be null.
     * @param precision the ChronoUnit to which the start and end instants should be truncated; must not be null.
     * @return a TemporalRange object from the specified start instant to the current instant, truncated to the given precision.
     */
    public static TemporalRange fromToNow(Instant start, ChronoUnit precision) {
        validateNotNull(start, "The start cannot be null.");
        validateNotNull(precision, "The precision cannot be null.");
        return new TemporalRange(start.truncatedTo(precision), Instant.now().truncatedTo(precision));
    }

    /**
     * Creates a TemporalRange starting from the specified start instant until the maximum possible instant.
     *
     * @param start the starting point in time of the range; must not be null.
     * @return a TemporalRange object from the specified start instant to the maximum possible end instant.
     */
    public static TemporalRange fromToMax(Instant start) {
        validateNotNull(start, "The start cannot be null.");
        return new TemporalRange(start, MAX);
    }

    /**
     * Creates a TemporalRange with a specified start instant and the maximum possible end instant,
     * both truncated to a given precision.
     *
     * @param start the starting point in time of the range
     * @param precision the ChronoUnit to which the start and end instants should be truncated
     * @return a TemporalRange object from the specified start instant to the maximum possible end instant,
     *         truncated to the given precision
     */
    public static TemporalRange fromToMax(Instant start, ChronoUnit precision) {
        validateNotNull(start, "The start cannot be null.");
        validateNotNull(precision, "The precision cannot be null.");
        return new TemporalRange(start.truncatedTo(precision), MAX.truncatedTo(precision));
    }

    /**
     * Checks if a collection of TemporalRange objects are contiguous.
     * Two temporal ranges are considered contiguous if one range meets or is met by the subsequent range.
     *
     * @param ranges the collection of TemporalRange objects to be checked
     * @return true if the TemporalRange objects in the collection are contiguous, otherwise false
     */
    public static boolean isContiguous(Collection<TemporalRange> ranges) {
        if (ranges == null || ranges.isEmpty()) {
            return true;
        }
        boolean isContiguous = true;
        Iterator<TemporalRange> iterator = ranges.iterator();
        TemporalRange current = iterator.next();
        while (iterator.hasNext()) {
            TemporalRange next = iterator.next();
            if (!current.isContiguousWith(next)) {
                isContiguous = false;
                break;
            }
            current = next;
        }
        return isContiguous;
    }

    
    /**
     * Adds the specified duration to both the start and end instants of this TemporalRange.
     *
     * @param duration the duration to add to this TemporalRange; must not be null and should be positive.
     * @return a new TemporalRange with the specified duration added to the start and end instants.
     */
    public TemporalRange plus(Duration duration) {
        validateNotNull(duration, "Duration cannot be null.");
        validateDuration(duration);
        return new TemporalRange(start.plus(duration), end.plus(duration));
    }

    
    /**
     * Returns a new TemporalRange by subtracting the specified duration from the start and end points of this range.
     *
     * @param duration the duration to subtract from this TemporalRange; must not be null.
     * @return a new TemporalRange with the duration subtracted from the start and end points.
     */
    public TemporalRange minus(Duration duration) {
        validateNotNull(duration, "Duration cannot be null.");
        validateDuration(duration);
        return new TemporalRange(start.minus(duration), end.minus(duration));
    }

    /**
     * Returns the duration of the temporal range in seconds.
     *
     * @return the number of seconds between the start and end instants of this temporal range.
     */
    public long durationInSeconds() {
        return end.getEpochSecond() - start.getEpochSecond();
    }

    /**
     * Compares this TemporalRange object with the specified TemporalRange for order.
     * Compares based on the start instants first, and if they are equal, compares based on the end instants.
     *
     * @param other the TemporalRange to be compared.
     * @return a negative integer, zero, or a positive integer as this TemporalRange is less than,
     * equal to, or greater than the specified TemporalRange based on their start and end instants.
     */
    @Override
    public int compareTo(TemporalRange other) {
        int startComparison = this.start.compareTo(other.start);
        return startComparison != 0 ? startComparison : this.end.compareTo(other.end);
    }

    /**
     * Checks if this TemporalRange ends before the start of another TemporalRange.
     *
     * @param other the TemporalRange to compare with; must not be null.
     * @return true if this TemporalRange ends before the other TemporalRange starts, otherwise false.
     */
    public boolean isBefore(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.end.isBefore(other.start);
    }

    /**
     * Checks if this TemporalRange starts after the end of another TemporalRange.
     *
     * @param other the TemporalRange to compare with; must not be null.
     * @return true if this TemporalRange starts after the other TemporalRange ends, otherwise false.
     */
    public boolean isAfter(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.start.isAfter(other.end);
    }

    public boolean meets(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.end.equals(other.start);
    }

    public boolean isMetBy(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.start.equals(other.end);
    }

    public boolean overlapsBefore(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.start.isBefore(other.start) && this.contains(other.end);
    }

    public boolean overlapsAfter(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.end.isAfter(other.end) && this.contains(other.start);
    }

    public boolean finishes(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.start.isAfter(other.start) && this.end.equals(other.end);
    }

    public boolean isFinishedBy(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.start.isBefore(other.start) && this.start.equals(other.start);
    }

    public boolean includes(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.start.isBefore(other.start) && this.end.isAfter(other.end);
    }

    public boolean isDuring(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.start.isAfter(other.start) && this.end.isBefore(other.end);
    }

    public boolean starts(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.end.isBefore(other.end) && this.start.equals(other.start);
    }

    public boolean isStartedBy(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.end.isAfter(other.end) && this.start.equals(other.start);
    }

    /**
     * Checks if this TemporalRange is contiguous with another TemporalRange.
     * Two ranges are considered contiguous if this {@link #meets(TemporalRange)} or {@link #isMetBy(TemporalRange)} 
     * other range
     *
     * @param other the TemporalRange to check for contiguity; can be null.
     * @return true if this TemporalRange is contiguous with the other TemporalRange, otherwise false.
     */
    public boolean isContiguousWith(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.meets(other) || this.isMetBy(other);
    }

    /**
     * Checks whether the specified instant is within the temporal range.
     *
     * @param instant the point in time to be checked; must not be null.
     * @return true if the instant is greater than or equal to start and less than end, otherwise false.
     */
    public boolean contains(Instant instant) {
        validateNotNull(instant, "The instant cannot be null.");
        return !instant.isBefore(start) && instant.isBefore(end);
    }

    /**
     * Checks if this TemporalRange overlaps with another TemporalRange.
     *
     * @param other the TemporalRange to check for overlap; must not be null.
     * @return true if the TemporalRange overlaps with the other TemporalRange, otherwise false.
     */
    public boolean overlaps(TemporalRange other) {
        validateNotNull(other, "The other range cannot be null.");
        return this.overlapsBefore(other) || this.overlapsAfter(other);
    }

    public TemporalRange withPrecision(ChronoUnit precision) {
        validateNotNull(precision, "The precision cannot be null.");
        return new TemporalRange(start.truncatedTo(precision), end.truncatedTo(precision));
    }

    private static void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void validateDuration(Duration duration) {
        if (duration.isNegative()) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
    }
}