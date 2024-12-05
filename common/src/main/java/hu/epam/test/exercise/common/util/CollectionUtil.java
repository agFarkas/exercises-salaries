package hu.epam.test.exercise.common.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public final class CollectionUtil {

    private CollectionUtil() {}

    public static boolean isEmpty(Collection<?> collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }
}
