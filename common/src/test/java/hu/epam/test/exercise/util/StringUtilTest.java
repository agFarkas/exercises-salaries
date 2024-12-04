package hu.epam.test.exercise.util;

import hu.epam.test.exercise.common.util.StringUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilTest {

    @ParameterizedTest
    @MethodSource("provideStringTestArgumets")
    void blankStringTest(String text, boolean expectedToBeBlank) {
        assertEquals(expectedToBeBlank, StringUtil.isBlank(text));
    }

    static Stream<Arguments> provideStringTestArgumets() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(" ", true),
                Arguments.of("      ", true),
                Arguments.of("", true),
                Arguments.of("\t", true),
                Arguments.of("\n", true),
                Arguments.of("\r\n", true),
                Arguments.of("This is a relevant test-text.", false)
        );
    }
}
