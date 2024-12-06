package hu.epam.test.exercise.operations.evaluation;

import hu.epam.test.exercise.model.Employee;

import java.util.List;

/**
 * The interface for evaluating functions for employees.
 * Should use evaluators.
 */
public interface EmployeeEvaluator {

    /**
     * Collects those managers from all the employees who earn more than the maximal, or less than the minimal rate of plus
     * to the average salary of their subordinates. A manager is an employee referred to by at least one other employee as their manager.
     * The minimum and maximum of this range are defined in {@link ManagerSalaryDifferenceEvaluator} class as constants.
     *
     * @implSpec Should include the pre-filtering for managers. Should return only the {@link ManagerSalaryDifferenceEvaluator}s
     * those managers who are out of the recommended range mentioned above.
     *
     * @param allEmployees All the employees (including non-managers)
     * @return A list of {@link ManagerSalaryDifferenceEvaluator}s of each manager whose salary is out of the recommended range
     * mentioned above.
     */
    List<ManagerSalaryDifferenceEvaluator> collectManagersEarningOutOfRecommendations(List<Employee> allEmployees);


    /**
     * Collects those employees who have a longer reporting line than the recommended maximum. This maximum number for
     * the length of reporting line is defined in {@link EmployeeReportingLineEvaluator} class as a constant.
     *
     * @param allEmployees All the employees
     * @return A list of {@link EmployeeReportingLineEvaluator} of each employee having the length of reporting line greater than
     * the limit mentioned above.
     */
    List<EmployeeReportingLineEvaluator> collectEmployeesWithTooLongReportingLines(List<Employee> allEmployees);
}
