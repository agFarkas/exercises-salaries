package hu.epam.test.exercise.evaluation.operations.evaluation;

import hu.epam.test.exercise.model.Employee;

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
        return calcualtePercentage(
                obtainPercentage()
        );
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
        return calculateMinimumToAverage().compareTo(new BigDecimal(manager.getSalary())) > 0;
    }

    public boolean isMoreThanMaximum() {
        return calculateMaximumToAverage().compareTo(new BigDecimal(manager.getSalary())) < 0;
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

    private BigDecimal obtainPercentage() {
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
