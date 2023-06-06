package ru.vzotov.loans.domain.model;

import ru.vzotov.domain.model.Money;

import java.time.LocalDate;

/**
 * Досрочный платеж
 */
public record ExtraPayment(LocalDate date, Money value) {

}
