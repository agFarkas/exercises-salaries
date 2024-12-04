package hu.epam.test.exercise.common.util;

import java.util.Objects;

public class StringUtil {

    private StringUtil() {}

    public static boolean isBlank(String text) {
        return Objects.isNull(text) || text.isBlank();
    }
}
