package ru.vzotov.loans.domain.model;

import ru.vzotov.calendar.domain.model.WorkCalendar;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Аннуитет альфабанка:
 * <a href="https://www.banki.ru/services/responses/bank/response/10170371/">https://www.banki.ru/services/responses/bank/response/10170371/</a>
 * <a href="https://mobile-testing.ru/grafik-platezhey-po-kreditu-v-alfa-banke/">https://mobile-testing.ru/grafik-platezhey-po-kreditu-v-alfa-banke/</a>
 */
public class AnnuityAlfa extends Annuity {

    public AnnuityAlfa(Money value,
                       LocalDate date,
                       AnnualPercentageRate rate,
                       int duration,
                       WorkCalendar calendar, int dayOfPayment) {
        super(value, date, rate, duration, new SmartRounding(), calendar, dayOfPayment);
    }

    /**
     * Расчет аннуитетного платежа
     *
     * @param debt остаток основного долга
     * @param n    количество платежей
     * @return Размер платежа
     */
    public Money calculatePayment(Money debt, int n) {
        // Процентная ставка за 1 месяц пользования средствами
        double p = rate().value() / 100.0 / 12.0;
        double k = p / (1.0 - Math.pow(1.0 + p, -n));
        return rounding().round(debt, debt.multiply(k));
    }

    /**
     * @param debt остаток основного долга
     * @return Размер процентов
     */
    public Money calculatePercent(Money debt, int i) {
        LocalDate baseDate = paymentDate(i - 1);
        LocalDate paymentDate = paymentDate(i);
        LocalDate border = paymentDate.withDayOfMonth(1);
        long n1 = ChronoUnit.DAYS.between(baseDate.plusDays(1), border);
        long n2 = ChronoUnit.DAYS.between(border, paymentDate.plusDays(1));

        double p = n1 * rate().value() / 100.0 / (Year.from(baseDate).length()) +
                n2 * rate().value() / 100.0 / (Year.from(paymentDate).length());
        return debt.multiply(p);
    }

    @Override
    public Schedule schedule(List<Schedule> past, int i) {
        Schedule prev = past.isEmpty() ? null : past.get(past.size() - 1);
        Money debt = prev == null ? value() : prev.debt().subtract(prev.payoff());
        final LocalDate paymentDate = paymentDate(i);
        Money cost = calculatePercent(debt, i);
        Money payment = calculatePayment(debt, duration() - i + 1);

        return new Schedule(
                i,
                paymentDate,
                nominalDate(i),
                duration() - i,
                debt,
                payment,
                payment.subtract(cost),
                cost
        );
    }
}
