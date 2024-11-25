package hu.epam.exercise.operations.mapping;

import hu.epam.exercise.TestParent;
import hu.epam.exercise.model.Employee;
import hu.epam.exercise.util.EmployeeUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeMapperTest extends TestParent {

    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    @Test
    void mapEmployeesTest() {
        var tableLines = readTableLines("employees-valid.csv");
        var employeeLines = EmployeeUtil.getEmployeeLines(tableLines);

        var employees = employeeMapper.mapEmployees(employeeLines);
        assertThat(employeeMapper.mapEmployees(employeeLines));

        assertThat(employees).hasSize(5);
        assertThat(employees.getFirst())
                .usingRecursiveComparison()
                .isEqualTo(Employee.of(123, "John", "Doe", 60000, null));

        assertThat(employees.get(1))
                .usingRecursiveComparison()
                .isEqualTo(Employee.of(124, "Martin", "Chekov", 45000, 123));
    }
}
