package hu.epam.test.exercise.evaluation.operations.evaluation;

import hu.epam.test.exercise.common.util.CollectionUtil;
import hu.epam.test.exercise.model.Employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ManagerSalaryDifferenceEvaluator {

    private final static BigDecimal MINIMUM_RECOMMENDED_PLUS_RATE = new BigDecimal("0.2");

    private final static BigDecimal MAXIMUM_RECOMMENDED_PLUS_RATE = new BigDecimal("0.5");

    private final Employee manager;
    private final List<Employee> subordinates;

    private final BigDecimal managerSalary;

    private final BigDecimal averageSalaryOfSubordinates;


    private BigDecimal rate;

    public ManagerSalaryDifferenceEvaluator(Employee manager, List<Employee> allEmployees) {
        this.manager = manager;
        this.managerSalary = BigDecimal.valueOf(manager.getSalary());

        this.subordinates = obtainSubordinates(manager, allEmployees);
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
        double average = subordinates.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);

        return round(average, 2);
    }

    private static double round(double average, int precision) {
        if(average == 0) {
            return 0;
        }

        var tenPow = Math.pow(10, precision);
        var avgToRound = average;

        avgToRound *= tenPow;
        avgToRound = Math.round(avgToRound);

        return avgToRound / tenPow;
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
        if (CollectionUtil.isEmpty(subordinates)) {
            return false;
        }

        return calculateMinimumToAverage().compareTo(managerSalary) > 0;
    }

    public boolean isMoreThanMaximum() {
        if (CollectionUtil.isEmpty(subordinates)) {
            return false;
        }

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

    public BigDecimal calculateDifferenceFromRecommendation() {
        if (isLessThanMinimum()) {
            return managerSalary.subtract(calculateMinimumToAverage());
        } else if (isMoreThanMaximum()) {
            return managerSalary.subtract(calculateMaximumToAverage());
        }

        return BigDecimal.ZERO;
    }

    public Employee getManager() {
        return manager;
    }

    public BigDecimal getAverageSalaryOfSubordinates() {
        return averageSalaryOfSubordinates;
    }

}
