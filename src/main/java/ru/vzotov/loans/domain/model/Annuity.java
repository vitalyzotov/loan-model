package ru.vzotov.loans.domain.model;

import ru.vzotov.calendar.domain.model.DayType;
import ru.vzotov.calendar.domain.model.WorkCalendar;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;

public abstract class Annuity implements LoanConditions {

    private final AnnualPercentageRate rate;

    private final Rounding rounding;

    private final WorkCalendar calendar;

    private final int dayOfPayment;

    /**
     * Дата получения кредита
     */
    private final LocalDate date;

    /**
     * Сумма кредита
     */
    private final Money value;

    /**
     * Срок кредита, месяцев
     */
    private final int duration;

    public Annuity(Money value, LocalDate date, AnnualPercentageRate rate, int duration, Rounding rounding, WorkCalendar calendar, int dayOfPayment) {
        this.value = value;
        this.date = date;
        this.rate = rate;
        this.duration = duration;
        this.rounding = rounding;
        this.calendar = calendar;
        this.dayOfPayment = dayOfPayment;
    }

    @Override
    public Money value() {
        return value;
    }

    @Override
    public LocalDate date() {
        return date;
    }

    @Override
    public int dayOfPayment() {
        return dayOfPayment;
    }

    @Override
    public int duration() {
        return duration;
    }

    @Override
    public WorkCalendar calendar() {
        return calendar;
    }

    public AnnualPercentageRate rate() {
        return rate;
    }

    public Rounding rounding() {
        return rounding;
    }

    @Override
    public LocalDate nominalDate(int i) {
        return date().plusMonths(i).withDayOfMonth(1).plusDays(dayOfPayment() - 1);
    }

    @Override
    public LocalDate paymentDate(int i) {
        if (i == 0) return date();

        LocalDate payment = nominalDate(i);
        while (calendar().typeOfDay(payment) != DayType.WORKING) {
            payment = payment.plusDays(1);
        }

        return payment;
    }
}
