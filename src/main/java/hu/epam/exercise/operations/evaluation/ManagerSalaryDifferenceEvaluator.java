package hu.epam.exercise.operations.evaluation;

import hu.epam.exercise.model.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ManagerSalaryDifferenceEvaluator {

    private final static BigDecimal MINIMUM_RECOMMENDED_PLUS_RATE = new BigDecimal("0.2");

    private final static BigDecimal MAXIMUM_RECOMMENDED_PLUS_RATE = new BigDecimal("0.5");

    private final Employee manager;

    private final BigDecimal averageSalaryOfSubordinates;

    private BigDecimal percentage;

    public ManagerSalaryDifferenceEvaluator(Employee manager, BigDecimal averageSalaryOfSubordinates) {
        this.manager = manager;
        this.averageSalaryOfSubordinates = averageSalaryOfSubordinates;
    }

    public static float getMinimumPlusSalaryRate() {
        return getRelativePercentageToAverage(MINIMUM_RECOMMENDED_PLUS_RATE);
    }

    public static float getMaximumPlusSalaryRate() {
        return getRelativePercentageToAverage(MAXIMUM_RECOMMENDED_PLUS_RATE);
    }

    public float getRelativePercentage() {
        return calcualtePercentage(percentage);
    }

    private static float getRelativePercentageToAverage(BigDecimal rate) {
        return calcualtePercentage(rate.add(new BigDecimal(1)));
    }

    private static float calcualtePercentage(BigDecimal absoluteRate) {
        return absoluteRate.multiply(new BigDecimal(100))
                .floatValue();
    }

    public boolean isOutOfRecommendations() {
        return isLessThanMinimum() || isMoreThanMaximum();
    }

    public boolean isLessThanMinimum() {
        return calculatePercentage().compareTo(MINIMUM_RECOMMENDED_PLUS_RATE) < 0;
    }

    public boolean isMoreThanMaximum() {
        return calculatePercentage().compareTo(MAXIMUM_RECOMMENDED_PLUS_RATE) > 0;
    }

    private BigDecimal calculatePercentage() {
        if (Objects.isNull(percentage)) {
            percentage = calculateDifference()
                    .divide(averageSalaryOfSubordinates, 2, RoundingMode.HALF_UP);
        }

        return percentage;
    }

    private BigDecimal calculateDifference() {
        var salary = new BigDecimal(manager.getSalary());
        return salary.subtract(averageSalaryOfSubordinates);
    }

    public Employee getManager() {
        return manager;
    }

    public BigDecimal getAverageSalaryOfSubordinates() {
        return averageSalaryOfSubordinates;
    }
}