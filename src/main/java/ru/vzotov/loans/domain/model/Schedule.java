package ru.vzotov.loans.domain.model;

import ru.vzotov.domain.model.Money;

import java.time.LocalDate;

public class Schedule {
    /**
     * Порядковый номер платежного периода
     */
    private int period;

    /**
     * Дата платежа
     */
    private LocalDate date;

    /**
     * Номинальная дата платежа исходя из условий кредита (без учета выходных, праздников)
     */
    private LocalDate nominalDate;

    /**
     * Количество платежных периодов
     */
    private int remainingPeriods;

    /**
     * Остаток задолженности
     */
    private Money debt;

    /**
     * Размер платежа
     */
    private Money payment;

    /**
     * Сумма в счет погашения основного долга
     */
    private Money payoff;

    /**
     * Сумма в счет погашения процентов
     */
    private Money cost;

    public Schedule(int period, LocalDate date, LocalDate nominalDate, int remainingPeriods, Money debt, Money payment, Money payoff, Money cost) {
        this.period = period;
        this.date = date;
        this.nominalDate = nominalDate;
        this.remainingPeriods = remainingPeriods;
        this.debt = debt;
        this.payment = payment;
        this.payoff = payoff;
        this.cost = cost;
    }

    public int period() {
        return period;
    }

    public LocalDate date() {
        return date;
    }

    public LocalDate nominalDate() {
        return nominalDate;
    }

    public int remainingPeriods() {
        return remainingPeriods;
    }

    public Money debt() {
        return debt;
    }

    public Money payment() {
        return payment;
    }

    public Money payoff() {
        return payoff;
    }

    public void payoffEarly(Money payoff) {
        this.payoff = this.payoff.add(payoff);
    }

    public Money cost() {
        return cost;
    }
}
