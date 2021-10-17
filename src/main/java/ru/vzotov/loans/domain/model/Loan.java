package ru.vzotov.loans.domain.model;

import ru.vzotov.domain.model.Money;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Кредит
 */
public class Loan {

    /**
     * Условия кредита
     */
    private final LoanConditions conditions;

    private final List<ExtraPayment> extraPayments;

    public Loan(LoanConditions conditions, List<ExtraPayment> extraPayments) {
        this.conditions = conditions;
        this.extraPayments = extraPayments;
    }

    public List<ExtraPayment> extraPayments() {
        return extraPayments;
    }

    public int dayOfPayment() {
        return conditions().dayOfPayment();
    }

    public Money value() {
        return conditions.value();
    }

    public LocalDate date() {
        return conditions.date();
    }

    public int duration() {
        return conditions.duration();
    }

    public LoanConditions conditions() {
        return conditions;
    }

    /**
     * Вычисляет даты платежей
     *
     * @return список дат платежей
     */
    public List<LocalDate> dates() {
        List<LocalDate> result = new ArrayList<>();

        LocalDate date;
        for (int i = 1, max = duration(); i <= max; i++) {
            date = conditions().paymentDate(i);
            result.add(date);
        }

        return result;
    }

    public List<Schedule> schedule() {
        List<Schedule> result = new ArrayList<>();

        ExtraPayment p;
        LinkedList<ExtraPayment> extra = extraPayments().stream().sorted(Comparator.comparing(ExtraPayment::date)).collect(Collectors.toCollection(LinkedList::new));
        for (int i = 1, max = duration(); i <= max; i++) {
            final Schedule s = conditions().schedule(result, i);
            result.add(s);
            p = extra.peek();
            while (p != null && !p.date().isAfter(s.date())) {
                s.payoffEarly(p.value());
                extra.poll();
                p = extra.peek();
            }
        }

        return result;
    }
}
