package ru.vzotov.loans.domain.model;

import ru.vzotov.domain.model.Money;

public class SmartRounding implements Rounding {
    @Override
    public Money round(Money context, Money value) {
        int round = context.rawAmount() > 100000000 ? 3 : 2;
        return value.roundUp(round);
    }
}
