package hu.epam.test.exercise.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.stream.DoubleStream;

/**
 * Utility class for mathematical operations
 */
public class MathUtil {

    private MathUtil() {
    }

    /**
     * calcuilates average of a {@link DoubleStream}
     *
     * @param doubleStream
     * @param precision    The precision of the average in the number of decimal places.
     * @return The calculated average wrapped into a BigDecimal. If the doubleStream is null or empty, returns zero.
     */
    public static BigDecimal average(DoubleStream doubleStream, int precision) {
        if (Objects.isNull(doubleStream)) {
            return BigDecimal.ZERO;
        }
        var average = BigDecimal.valueOf(doubleStream.average()
                        .orElse(0))
                .setScale(precision, RoundingMode.HALF_UP);

        if (average.doubleValue() == 0) {
            return BigDecimal.ZERO;
        }

        return average;
    }
}
