package hu.epam.test.exercise.util;

import hu.epam.test.exercise.common.util.MathUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathUtilTest {

    @ParameterizedTest
    @MethodSource("provideArgumets")
    void averageTest(DoubleStream doubleStream, int precision, BigDecimal expectedAverage) {
        assertEquals(expectedAverage, MathUtil.average(doubleStream, precision));
    }

    static Stream<Arguments> provideArgumets() {
        return Stream.of(
                Arguments.of(null, 2, BigDecimal.ZERO),
                Arguments.of(doubleStreamOf(), 2, BigDecimal.ZERO),
                Arguments.of(doubleStreamOf(0.25567, 0.32, 10, 1.263), 2, new BigDecimal("2.96")),
                Arguments.of(doubleStreamOf(1, 1, 1), 2, new BigDecimal("1.00")),
                Arguments.of(doubleStreamOf(1, 1, 1), 3, new BigDecimal("1.000"))
        );
    }

    private static DoubleStream doubleStreamOf(double... doubles) {
        return DoubleStream.of(doubles);
    }
}
