package ru.vzotov.loans.domain.model;

import ru.vzotov.domain.model.Money;

import java.time.LocalDate;

/**
 * Досрочный платеж
 */
public class ExtraPayment {

    private LocalDate date;

    private Money value;

    public ExtraPayment(LocalDate date, Money value) {
        this.date = date;
        this.value = value;
    }

    public LocalDate date() {
        return date;
    }

    public Money value() {
        return value;
    }
}
