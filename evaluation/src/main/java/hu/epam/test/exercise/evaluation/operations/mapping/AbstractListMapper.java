package hu.epam.test.exercise.evaluation.operations.mapping;

import java.util.List;

public abstract class AbstractListMapper<SOURCE, RESULT> {

    public List<RESULT> mapAll(List<SOURCE> employeeLines) {
        return employeeLines.stream()
                .map(this::mapElement)
                .toList();
    }

    protected abstract RESULT mapElement(SOURCE strings);
}
