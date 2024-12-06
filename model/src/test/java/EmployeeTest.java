import hu.epam.test.exercise.model.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    @Test
    void ceoEmployeeTest() {
        var employee = Employee.of(1, "John", "Doe", 50000, null);

        assertEquals(1, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals(50000, employee.getSalary());
        assertNull(employee.getManagerId());
        assertTrue(employee.isCEO());
    }

    @Test
    void nonCeoEmployeeTest() {
        var employee = Employee.of(2, "Marty", "McFly", 50000, 1);

        assertEquals(2, employee.getId());
        assertEquals("Marty", employee.getFirstName());
        assertEquals("McFly", employee.getLastName());
        assertEquals(50000, employee.getSalary());
        assertEquals(1, employee.getManagerId());
        assertFalse(employee.isCEO());
    }
}
