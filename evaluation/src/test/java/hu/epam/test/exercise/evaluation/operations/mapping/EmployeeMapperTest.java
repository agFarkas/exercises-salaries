package hu.epam.test.exercise.evaluation.operations.mapping;

import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeMapperTest {

    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    @Test
    void mapEmployeesTest() {
        List<String[]> employeeLines = List.of(
                new String[] {"123","John","Doe","60000"},
                new String[] {"124","Martin","Chekov","45000","123"},
                new String[] {"125","Bob","Ronstad","47000","123"},
                new String[] {"300","Alice","Hasacat","50000","124"},
                new String[] {"305","Brett","Hardleaf","34000","300"}
        );

        var employees = employeeMapper.mapEmployees(employeeLines);

        assertThat(employees).hasSize(5);
        assertThat(employees.getFirst())
                .usingRecursiveComparison()
                .isEqualTo(Employee.of(123, "John", "Doe", 60000, null));

        assertThat(employees.get(1))
                .usingRecursiveComparison()
                .isEqualTo(Employee.of(124, "Martin", "Chekov", 45000, 123));
    }
}
