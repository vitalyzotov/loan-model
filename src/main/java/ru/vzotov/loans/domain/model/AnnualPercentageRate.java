package ru.vzotov.loans.domain.model;

public class AnnualPercentageRate {

    private final double value;

    public AnnualPercentageRate(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }
}
