package hu.epam.test.exercise.evaluation.operations.evaluation;

import hu.epam.test.exercise.model.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class ManagerSalaryDifferenceEvaluator {

    private final static BigDecimal MINIMUM_RECOMMENDED_PLUS_RATE = new BigDecimal("0.2");

    private final static BigDecimal MAXIMUM_RECOMMENDED_PLUS_RATE = new BigDecimal("0.5");

    private final Employee manager;

    private final BigDecimal managerSalary;

    private final BigDecimal averageSalaryOfSubordinates;

    private BigDecimal rate;

    public ManagerSalaryDifferenceEvaluator(Employee manager, List<Employee> allEmployees) {
        this.manager = manager;
        this.managerSalary = BigDecimal.valueOf(manager.getSalary());

        var subordinates = obtainSubordinates(manager, allEmployees);
        this.averageSalaryOfSubordinates = BigDecimal.valueOf(calculateAverageSalary(subordinates));
    }

    private List<Employee> obtainSubordinates(Employee manager, List<Employee> allEmployees) {
        return allEmployees.stream()
                .filter(employee -> isSubordinateOf(manager, employee))
                .toList();
    }

    private boolean isSubordinateOf(Employee manager, Employee employee) {
        return Objects.equals(employee.getManagerId(), manager.getId());
    }

    private static double calculateAverageSalary(List<Employee> subordinates) {
        return subordinates.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }

    public static float getMinimumPlusSalaryRate() {
        return getRelativePercentageToAverage(MINIMUM_RECOMMENDED_PLUS_RATE);
    }

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

    public boolean isOutOfRecommendations() {
        return isLessThanMinimum() || isMoreThanMaximum();
    }

    public boolean isLessThanMinimum() {
        return calculateMinimumToAverage().compareTo(managerSalary) > 0;
    }

    public boolean isMoreThanMaximum() {
        return calculateMaximumToAverage().compareTo(managerSalary) < 0;
    }

    private BigDecimal calculateMinimumToAverage() {
        return averageSalaryOfSubordinates.multiply(
                MINIMUM_RECOMMENDED_PLUS_RATE.add(BigDecimal.ONE)
        );
    }

    private BigDecimal calculateMaximumToAverage() {
        return averageSalaryOfSubordinates.multiply(
                MAXIMUM_RECOMMENDED_PLUS_RATE.add(BigDecimal.ONE)
        );
    }

    private BigDecimal obtainRate() {
        if (Objects.isNull(rate)) {
            rate = calculateDifference()
                    .divide(averageSalaryOfSubordinates, 2, RoundingMode.HALF_UP);
        }

        return rate;
    }

    private BigDecimal calculateDifference() {
        return managerSalary.subtract(averageSalaryOfSubordinates);
    }

    public BigDecimal calculateDifferenceFromRecommendation() {
        if(isLessThanMinimum()) {
            return managerSalary.subtract(calculateMinimumToAverage());
        } else if (isMoreThanMaximum()) {
            return managerSalary.subtract(calculateMaximumToAverage());
        }

        return managerSalary;
    }

    public Employee getManager() {
        return manager;
    }

    public BigDecimal getAverageSalaryOfSubordinates() {
        return averageSalaryOfSubordinates;
    }

}
