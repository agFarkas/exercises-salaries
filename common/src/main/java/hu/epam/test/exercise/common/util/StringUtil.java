package hu.epam.test.exercise.common.util;

import java.util.Objects;

/**
 * Utility class to provide null-safe operations on string.
 */
public final class StringUtil {

    private StringUtil() {}

    /**
     * Checks if text is null, empty, or contains only white spaces.
     *
     * @param text
     * @return {@code true} if text is null, empty, or contains only white spaces.
     */
    public static boolean isBlank(String text) {
        return Objects.isNull(text) || text.isBlank();
    }
}
