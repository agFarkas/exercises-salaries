package hu.epam.test.exercise.util;

import hu.epam.test.exercise.common.util.CollectionUtil;
import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CollectionUtilTest {

    @ParameterizedTest
    @MethodSource("provideCollectionTestArgumets")
    void emptyCollectionTest(Collection<?> collection, boolean expectedToBeBlank) {
        assertEquals(expectedToBeBlank, CollectionUtil.isEmpty(collection));
    }

    static Stream<Arguments> provideCollectionTestArgumets() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(Collections.emptyList(), true),
                Arguments.of(Set.of(), true),
                Arguments.of(Set.of(
                        Employee.of(1, "John", "Doe", 20000, null),
                        Employee.of(2, "Marty", "McFly", 17200, 1)
                ), false)
        );
    }
}
