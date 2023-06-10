package ru.vzotov.loans.domain.model;

import ru.vzotov.domain.model.Money;

public class SimpleRounding implements Rounding {

    private final int power;

    public SimpleRounding(int power) {
        this.power = power;
    }

    @Override
    public Money round(Money context, Money value) {
        return value.roundUp(power);
    }
}
