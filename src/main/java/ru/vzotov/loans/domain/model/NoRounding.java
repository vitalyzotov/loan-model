package ru.vzotov.loans.domain.model;

import ru.vzotov.domain.model.Money;

public class NoRounding implements Rounding {
    @Override
    public Money round(Money context, Money value) {
        return value;
    }
}
