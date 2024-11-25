package hu.epam.exercise.operations.mapping;

import hu.epam.exercise.model.Employee;
import org.springframework.stereotype.Component;

import java.util.List;

import static hu.epam.exercise.util.EmployeeUtil.*;
import static hu.epam.exercise.model.EmployeeField.*;

@Component
public class EmployeeMapper {

    public List<Employee> mapEmployees(List<String[]> employeeLines) {
        return employeeLines.stream()
                .map(this::mapEmployee)
                .toList();
    }
    private Employee mapEmployee(String[] employeeLine) {
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
