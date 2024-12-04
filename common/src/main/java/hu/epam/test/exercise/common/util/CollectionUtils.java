package hu.epam.test.exercise.common.util;

import java.util.Collection;
import java.util.Objects;

public class CollectionUtils {

    private CollectionUtils() {}

    public static boolean isEmpty(Collection collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }
}
