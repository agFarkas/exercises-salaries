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

    private double calculateAverageSalary(List<Employee> subordinates) {
        double average = subordinates.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);

        return round(average, 2);
    }

    private static double round(double average, int precision) {
        if (average == 0) {
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

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public boolean isLessThanMinimum() {
        return getAverageSalaryOfSubordinates()
                .multiply(
                        MINIMUM_RECOMMENDED_PLUS_RATE.add(BigDecimal.ONE)
                ).compareTo(getManagerSalary()) > 0;
    }

    public boolean isMoreThanMaximum() {
        return getAverageSalaryOfSubordinates()
                .multiply(
                        MAXIMUM_RECOMMENDED_PLUS_RATE.add(BigDecimal.ONE)
                ).compareTo(getManagerSalary()) < 0;
    }

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

    public BigDecimal getManagerSalary() {
        if (Objects.isNull(managerSalary)) {
            managerSalary = BigDecimal.valueOf(getManager().getSalary());
        }

        return managerSalary;
    }

    public Employee getManager() {
        return manager;
    }

    public BigDecimal getAverageSalaryOfSubordinates() {
        if (CollectionUtil.isEmpty(subordinates)) {
            return BigDecimal.ZERO;
        }

        if (Objects.isNull(averageSalaryOfSubordinates)) {
            averageSalaryOfSubordinates = BigDecimal.valueOf(calculateAverageSalary(subordinates));
        }
        return averageSalaryOfSubordinates;
    }

}
