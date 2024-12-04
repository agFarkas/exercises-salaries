package hu.epam.test.exercise.evaluation.operations.mapping;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    private final EmployeeListMapper employeeMapper = new EmployeeListMapper();

    @Test
    void mapEmployeesTest() {
        List<String[]> employeeLines = List.of(
                new String[] {"123","John","Doe","60000"},
                new String[] {"124","Martin","Chekov","45000","123"},
                new String[] {"125","Bob","Ronstad","47000","123"},
                new String[] {"300","Alice","Hasacat","50000","124"},
                new String[] {"305","Brett","Hardleaf","34000","300"}
        );

        var employees = employeeMapper.mapAll(employeeLines);

        assertEquals(5, employees.size());
        var employee1 = employees.getFirst();
        assertAll(
                () ->assertEquals(123, employee1.getId()),
                () ->assertEquals("John", employee1.getFirstName()),
                () ->assertEquals("Doe", employee1.getLastName()),
                () ->assertEquals(60000, employee1.getSalary()),
                () -> assertNull(employee1.getManagerId())
        );


        var employee2 = employees.get(1);
        assertAll(
                () ->assertEquals(124, employee2.getId()),
                () ->assertEquals("Martin", employee2.getFirstName()),
                () ->assertEquals("Chekov", employee2.getLastName()),
                () ->assertEquals(45000, employee2.getSalary()),
                () -> assertEquals(123, employee2.getManagerId())
        );
    }
}
