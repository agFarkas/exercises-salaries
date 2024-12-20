package hu.epam.test.exercise.util;

import hu.epam.test.exercise.common.model.EmployeeField;
import hu.epam.test.exercise.common.util.EmployeeUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static hu.epam.test.exercise.common.model.EmployeeField.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeUtilTest {

    List<String[]> tableLines;

    @BeforeAll
    public void init() {
        this.tableLines = List.of(
                new String[] {ID.getFieldName(), FIRST_NAME.getFieldName(), LAST_NAME.getFieldName(), SALARY.getFieldName(), MANAGER_ID.getFieldName()},
                new String[] {"123","John","Doe","60000"},
                new String[] {"124","Martin","Chekov","45000","123"},
                new String[] {"125","Bob","Ronstad","47000","123"},
                new String[] {"300","Alice","Hasacat","50000","124"},
                new String[] {"305","Brett","Hardleaf","34000","300"}
        );
    }

    @Test
    void getNumberOfFieldNamesTest() {
        assertEquals(5, EmployeeUtil.getNumberOfFieldNames());
    }

    @Test
    void getHeaderLineTest() {
        var headerLine = EmployeeUtil.getHeaderLine(tableLines);

        assertEquals(5, headerLine.size());
        assertIterableEquals(List.of(
                EmployeeField.ID.getFieldName(),
                EmployeeField.FIRST_NAME.getFieldName(),
                EmployeeField.LAST_NAME.getFieldName(),
                EmployeeField.SALARY.getFieldName(),
                EmployeeField.MANAGER_ID.getFieldName()
        ), headerLine);
    }

    @Test
    void getEmployeeLinesTest() {
        var employeeLines = new ArrayList<>(EmployeeUtil.getEmployeeLines(tableLines));
        assertEquals(5, employeeLines.size());

        var expectedList = List.of(
                new String[] {"123","John","Doe","60000"},
                new String[] {"124","Martin","Chekov","45000","123"},
                new String[] {"125","Bob","Ronstad","47000","123"},
                new String[] {"300","Alice","Hasacat","50000","124"},
                new String[] {"305","Brett","Hardleaf","34000","300"}
        );

        for (var i = 0; i < employeeLines.size(); i++) {
            assertArrayEquals(expectedList.get(i), employeeLines.get(i));
        }
    }

    @Test
    void getValueOfCeoTest() {
        var employeeLines = EmployeeUtil.getEmployeeLines(tableLines);
        var firstEmployeeLine = employeeLines.getFirst();

        assertEquals("John", EmployeeUtil.getValue(firstEmployeeLine, EmployeeField.FIRST_NAME));
        assertNull(EmployeeUtil.getValue(firstEmployeeLine, EmployeeField.MANAGER_ID));
    }

    @Test
    void getValueOfOtherEmployeeTest() {
        var employeeLines = EmployeeUtil.getEmployeeLines(tableLines);
        var secondEmployeeLine = employeeLines.get(1);

        assertEquals("Martin", EmployeeUtil.getValue(secondEmployeeLine, EmployeeField.FIRST_NAME));
        assertEquals("123", EmployeeUtil.getValue(secondEmployeeLine, EmployeeField.MANAGER_ID));
    }

    @Test
    void indexOfFieldNameTest() {
        assertEquals(0, EmployeeUtil.indexOfFieldName(EmployeeField.ID));
        assertEquals(1, EmployeeUtil.indexOfFieldName(EmployeeField.FIRST_NAME));
        assertEquals(2, EmployeeUtil.indexOfFieldName(EmployeeField.LAST_NAME));
        assertEquals(3, EmployeeUtil.indexOfFieldName(EmployeeField.SALARY));
        assertEquals(4, EmployeeUtil.indexOfFieldName(EmployeeField.MANAGER_ID));
    }
}
