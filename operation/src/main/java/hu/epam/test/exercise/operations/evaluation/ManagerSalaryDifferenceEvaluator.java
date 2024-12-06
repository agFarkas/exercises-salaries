package hu.epam.test.exercise.operations.evaluation;

import hu.epam.test.exercise.model.Employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static hu.epam.test.exercise.common.util.MathUtil.average;


/**
 * Evaluator class for operations about how the salary of one manager are compare to the average salary of their
 * subordinates. The recommended rates of the manager's plus salary are defined in {@code MINIMUM_RECOMMENDED_PLUS_RATE} and
 * {@code MAXIMUM_RECOMMENDED_PLUS_RATE}.
 */
public class ManagerSalaryDifferenceEvaluator {

    private static final int ROUNDING_PRECISION = 2;

    private final static BigDecimal MINIMUM_RECOMMENDED_PLUS_RATE = new BigDecimal("0.2");

    private final static BigDecimal MAXIMUM_RECOMMENDED_PLUS_RATE = new BigDecimal("0.5");

    private final Employee manager;

    private final List<Employee> subordinates;

    private BigDecimal managerSalary;

    private BigDecimal averageSalaryOfSubordinates;


    private BigDecimal rate;

    public ManagerSalaryDifferenceEvaluator(Employee manager, List<Employee> allEmployees) {
        this.manager = manager;
        this.subordinates = obtainSubordinates(manager, allEmployees);
    }

    private List<Employee> obtainSubordinates(Employee manager, List<Employee> allEmployees) {
        return allEmployees.stream()
                .filter(employee -> isSubordinateOf(manager, employee))
                .toList();
    }

    private boolean isSubordinateOf(Employee manager, Employee employee) {
        return Objects.equals(employee.getManagerId(), manager.getId());
    }

    private BigDecimal calculateAverageSalary(List<Employee> subordinates) {
        return average(subordinates.stream()
                .mapToDouble(Employee::getSalary), ROUNDING_PRECISION);
    }

    /**
     * @return the multiplicator of the manager's recommended minimal salary of any average salary of the manager's subordinates.
     */
    public static float getMinimumPlusSalaryRate() {
        return getRelativePercentageToAverage(MINIMUM_RECOMMENDED_PLUS_RATE);
    }

    /**
     * @return the multiplicator of the manager's recommended maximal salary of any average salary of the manager's subordinates.
     */
    public static float getMaximumPlusSalaryRate() {
        return getRelativePercentageToAverage(MAXIMUM_RECOMMENDED_PLUS_RATE);
    }

    private static float getRelativePercentageToAverage(BigDecimal rate) {
        return calcualtePercentage(rate.add(BigDecimal.ONE));
    }

    private static float calcualtePercentage(BigDecimal absoluteRate) {
        return absoluteRate.multiply(BigDecimal.valueOf(100))
                .floatValue();
    }

    /**
     * Checks if the manager's salary is either less than its recommended minimum or more than the recommended maximum
     * calculated by their subordinates' average salary.
     *
     * @return {@code true} if the manager's salary is out of either bound of its recommended range. Otherwise {@code false}
     */
    public boolean isOutOfRecommendations() {
        return isLessThanMinimum() || isMoreThanMaximum();
    }

    /**
     * Checks if the manager's salary is less than its recommended minimum calculated by their subordinates' average salary.
     *
     * @return {@code true} if so.
     */
    public boolean isLessThanMinimum() {
        return getAverageSalaryOfSubordinates()
                .multiply(
                        MINIMUM_RECOMMENDED_PLUS_RATE.add(BigDecimal.ONE)
                ).compareTo(getManagerSalary()) > 0;
    }

    /**
     * Checks if the manager's salary is more than its recommended maximum calculated by their subordinates' average salary.
     *
     * @return {@code true} if so.
     */
    public boolean isMoreThanMaximum() {
        return getAverageSalaryOfSubordinates()
                .multiply(
                        MAXIMUM_RECOMMENDED_PLUS_RATE.add(BigDecimal.ONE)
                ).compareTo(getManagerSalary()) < 0;
    }

    /**
     * Calculates the difference from the recommended bounds.
     *
     * @return The signed difference from the recommended bounds if the manager's salary is out of them. Otherwise, returns 0.
     */
    public BigDecimal calculateDifferenceFromRecommendation() {
        if (isLessThanMinimum()) {
            return getManagerSalary().subtract(getAverageSalaryOfSubordinates()
                    .multiply(
                            MINIMUM_RECOMMENDED_PLUS_RATE.add(BigDecimal.ONE)
                    ));
        } else if (isMoreThanMaximum()) {
            return getManagerSalary().subtract(getAverageSalaryOfSubordinates()
                    .multiply(
                            MAXIMUM_RECOMMENDED_PLUS_RATE.add(BigDecimal.ONE)
                    ));
        }

        return BigDecimal.ZERO;
    }

    /**
     * @return The manager's salary wrapped in {@link BigDecimal}.
     */
    public BigDecimal getManagerSalary() {
        if (Objects.isNull(managerSalary)) {
            managerSalary = BigDecimal.valueOf(getManager().getSalary());
        }

        return managerSalary;
    }

    /**
     * @return The reference of the manager.
     */
    public Employee getManager() {
        return manager;
    }

    /**
     * Calculates the average salary of the manager's subordinates. For one manager, the calculation is only carried out
     * once.
     *
     * @return The average salary of the manager's subordinates.
     */
    public BigDecimal getAverageSalaryOfSubordinates() {
        if (Objects.isNull(averageSalaryOfSubordinates)) {
            averageSalaryOfSubordinates = calculateAverageSalary(subordinates);
        }

        return averageSalaryOfSubordinates;
    }
}
