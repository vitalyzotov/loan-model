package ru.vzotov.loans.domain.model;

import ru.vzotov.domain.model.Money;

public interface Rounding {
    Money round(Money context, Money value);
}
