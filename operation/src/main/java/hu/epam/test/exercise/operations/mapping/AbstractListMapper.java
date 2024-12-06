package hu.epam.test.exercise.operations.mapping;

import java.util.List;

/**
 * The abstract parent of mappers
 *
 * @param <SOURCE> The type of the source data
 * @param <RESULT> The type of the expected result data.
 */
public abstract class AbstractListMapper<SOURCE, RESULT> {

    /**
     * Maps a list of the source datas to a list of the result datas.
     *
     * @param sourceDatas A list of the source datas of the defined SOURCE type.
     * @return A list of the mapped result datas.
     */
    public List<RESULT> mapAll(List<SOURCE> sourceDatas) {
        return sourceDatas.stream()
                .map(this::mapElement)
                .toList();
    }

    /**
     * @param source A SOURCE-typed object
     * @return a RESULT-typed object.
     * @implSpec Should be implemented in any subclass.
     */
    protected abstract RESULT mapElement(SOURCE source);
}
