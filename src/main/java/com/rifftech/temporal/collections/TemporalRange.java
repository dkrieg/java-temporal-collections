package com.rifftech.temporal.collections;

import java.time.Duration;
import java.time.Instant;

/**
 * Represents a range of time defined by a start and an end {@link Instant}.
 * This record implements {@link Comparable<TemporalRange>} for natural ordering.
 *
 * @param start the starting {@link Instant} of the range, must be less than {@code end}
 * @param end the ending {@link Instant} of the range, must be greater than {@code start}
 */
public record TemporalRange(Instant start, Instant end) implements Comparable<TemporalRange> {

    /**
     * A constant TemporalRange representing an infinite duration starting from the earliest possible instant
     * to the latest possible instant. This can be used to denote a time range that effectively has no bounds
     * or limitations and is intended to be used in contexts where an unbounded temporal range is required.
     */
    public static final TemporalRange FOREVER = new TemporalRange(Instant.MIN, Instant.MAX);

    private static void validateInstants(Instant start, Instant end) {
        if (start == null || end == null || !start.isBefore(end)) {
            throw new IllegalArgumentException("Start must be before end and neither can be null.");
        }
    }

    /**
     * Constructs a TemporalRange object and validates that the start and end instants are not null
     * and that the start is before the end.
     *
     * @param start the starting point in time of the range
     * @param end the ending point in time of the range
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

    /**
     * Creates a TemporalRange starting from the current instant until the maximum possible instant.
     *
     * @return a TemporalRange object from the current instant to Instant.MAX
     */
    public static TemporalRange nowUntilMax() {
        return new TemporalRange(Instant.now(), Instant.MAX);
    }

    /**
     * Creates a TemporalRange starting from the current instant and lasting for a specified duration.
     *
     * @param duration the duration for which the temporal range should last
     * @return a TemporalRange object representing the time from now until the specified duration has passed
     */
    public static TemporalRange nowFor(Duration duration) {
        Instant now = Instant.now();
        return new TemporalRange(now, now.plus(duration));
    }

    /**
     * Creates a TemporalRange from the given*/
    public static TemporalRange fromTo(Instant start, Instant end) {
        return new TemporalRange(start, end);
    }

    /**
     * Creates a TemporalRange starting from the specified start instant until the current instant.
     *
     * @param start the starting point in time of the range
     * @return a TemporalRange object from the specified start instant to the current instant
     */
    public static TemporalRange fromToNow(Instant start) {
        return new TemporalRange(start, Instant.now());
    }

    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateDuration(Duration duration) {
        if (duration.isNegative()) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
    }

    /**
     * Checks whether the specified instant is within the temporal range.
     *
     * @param instant the point in time to be checked; must not be null.
     * @return true if the instant is within the range, otherwise false.
     */
    public boolean contains(Instant instant) {
        return instant != null && !instant.isBefore(start) && instant.isBefore(end);
    }

    /**
     * Checks if this TemporalRange overlaps with another TemporalRange.
     *
     * @param other the TemporalRange to check for overlap; must not be null.
     * @return true if the TemporalRange overlaps with the other TemporalRange, otherwise false.
     */
    public boolean overlaps(TemporalRange other) {
        return other != null && this.start.isBefore(other.end) && this.end.isAfter(other.start);
    }

    /**
     * Returns a new TemporalRange by adding the specified duration to the start and end points of this range.
     *
     * @param duration the duration to add to this TemporalRange.
     * @return a new TemporalRange with the duration added to the start and end points.
     */
    public TemporalRange plus(Duration duration) {
        validateDuration(duration);
        return new TemporalRange(start.plus(duration), end.plus(duration));
    }

    /**
     * Returns a new TemporalRange by subtracting the specified duration from the start and end points of this range.
     *
     * @param duration the duration to subtract from this TemporalRange; must not be negative.
     * @return a new TemporalRange with the duration subtracted from the start and end points.
     */
    public TemporalRange minus(Duration duration) {
        validateDuration(duration);
        return new TemporalRange(start.minus(duration), end.minus(duration));
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

    /**
     * Checks if this TemporalRange is contiguous with another TemporalRange.
     * Two ranges are considered contiguous if the end of this range is exactly
     * the start of the other range or if the start of this range is exactly
     * the end of the other range.
     *
     * @param other the TemporalRange to check for contiguity; can be null.
     * @return true if this TemporalRange is contiguous with the other TemporalRange, otherwise false.
     */
    public boolean isContiguousWith(TemporalRange other) {
        return other != null && (this.end.equals(other.start) || this.start.equals(other.end));
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
}