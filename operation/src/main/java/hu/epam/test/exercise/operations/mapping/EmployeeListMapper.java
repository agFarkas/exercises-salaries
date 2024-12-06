package hu.epam.test.exercise.operations.mapping;

import hu.epam.test.exercise.model.Employee;

import static hu.epam.test.exercise.common.model.EmployeeField.*;
import static hu.epam.test.exercise.common.util.EmployeeUtil.getValue;
import static hu.epam.test.exercise.common.util.EmployeeUtil.indexOfFieldName;


/**
 * Responsible for mapping a String array into an {@link Employee} object.
 */
public class EmployeeListMapper extends AbstractListMapper<String[], Employee> {

    /**
     *
     * @param employeeLine A String array with the proper datas of one employee
     * @return an {@link Employee} object by the employeeLine
     */
    @Override
    protected Employee mapElement(String[] employeeLine) {
        return Employee.of(
                Integer.parseInt(getValue(employeeLine, ID)),
                getValue(employeeLine, FIRST_NAME),
                getValue(employeeLine, LAST_NAME),
                Integer.parseInt(getValue(employeeLine, SALARY)),
                obtainManagerId(employeeLine)
        );
    }

    private Integer obtainManagerId(String[] employeeLine) {
        if(employeeLine.length > indexOfFieldName(MANAGER_ID)) {
            return Integer.parseInt(getValue(employeeLine, MANAGER_ID));
        }

        return null;
    }
}
