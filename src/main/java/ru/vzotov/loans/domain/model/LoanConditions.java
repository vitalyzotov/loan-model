package ru.vzotov.loans.domain.model;

import ru.vzotov.calendar.domain.model.WorkCalendar;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;
import java.util.List;

/**
 * Условия кредита
 */
public interface LoanConditions {

    /**
     * Сумма кредита
     * @return сумма
     */
    Money value();

    /**
     * Дата выдачи кредита
     * @return дата
     */
    LocalDate date();

    int dayOfPayment();

    int duration();

    WorkCalendar calendar();

    /**
     * Дата платежа, рассчитываемая по условиям кредита без учета выходных и праздничных дней.
     * @param i порядковый номер платежа
     * @return дата
     */
    LocalDate nominalDate(int i);

    LocalDate paymentDate(int i);

    Schedule schedule(List<Schedule> past, int i);
}
