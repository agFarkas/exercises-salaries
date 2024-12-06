package hu.epam.test.exercise.common.util;

import java.util.Collection;
import java.util.Objects;

/**
 * Utility class to provide null-safe operations on collections.
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    /**
     * Checks if collection is null or contains no elements
     *
     * @param collection
     * @return {@code true} if collection is null or contains no elements
     */
    public static boolean isEmpty(Collection<?> collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }
}
