package ru.vzotov.loans.domain.model;

import ru.vzotov.calendar.domain.model.WorkCalendar;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 * Аннуитет Газпромбанка
 * </p>
 *
 * <p>4.7. Проценты за пользование Кредитом уплачиваются Заемщиком со дня, следующего за
 * днем предоставления Кредита/Транша, до даты возврата Кредита, предусмотренной в
 * Индивидуальных условиях, либо до даты возврата Кредита в полном объеме (включительно),
 * указанной в требовании о полном досрочном погашении Заемщиком задолженности, направленном
 * Кредитором в порядке, предусмотренном п. 6.2.3 Общих условий Кредитного договора
 *
 * <p>
 * 4.12. При погашении Кредита Аннуитетными платежами Заемщик производит ежемесячные
 * платежи, за исключением первого и последнего платежей, по возврату Кредита и уплате
 * начисленных процентов, размер которых определяется по формуле, которая зависит от программы
 * кредитования
 * </p>
 *
 * <pre>
 * Размер                            ПС
 * ежемесячного = ОСЗ Х ----------------------------
 * аннуитетного              1 - (1 + ПС) – n
 * платежа
 * </pre>
 * <p>где:
 * <p>ОСЗ – остаток суммы Кредита на расчетную дату (в валюте Кредита);
 * <p>ПС – месячная процентная ставка, равная 1/12 от годовой процентной ставки, установленной в
 * соответствии с Кредитным договором (в процентах годовых).
 * <p>n – количество плановых платежей по основному долгу от даты расчета до даты полного возврата
 * Кредита.
 *
 * <p>4.14. Последний платеж по Кредиту включает в себя платеж по возврату всей оставшейся
 * суммы Кредита и уплате начисленных процентов и подлежит внесению не позднее
 * даты, определенной Индивидуальными условиями.
 */
public class AnnuityGpb extends Annuity {

    public AnnuityGpb(Money value,
                      LocalDate date,
                      AnnualPercentageRate rate,
                      int duration,
                      WorkCalendar calendar, int dayOfPayment) {
        super(value, date, rate, duration, new SimpleRounding(0), calendar, dayOfPayment);
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
     * @param debt остаток задолженности
     * @param i    порядковый номер платежа
     * @return Размер процентов
     */
    public Money calculatePercent(Money debt, Money previousPayoff, int i) {
        LocalDate d1 = (i == 1) ? date() : nominalDate(i - 1);
        LocalDate d2 = nominalDate(i);

        LocalDate border = d2.withDayOfMonth(1);
        long n1 = ChronoUnit.DAYS.between(d1.plusDays(1), border);
        long n2 = ChronoUnit.DAYS.between(border, d2.plusDays(1));
        Money payoffFee = null;
        if (i > 1) {
            long n3 = ChronoUnit.DAYS.between(nominalDate(i - 1), paymentDate(i - 1));
            payoffFee = previousPayoff == null ? null : previousPayoff.multiply(n3 * rate().value() / 100.0 / (Year.from(d1).length()));
        }

        Money m1 = debt.multiply(n1 * rate().value()).multiply(1.0d / 100.0d / (Year.from(d1).length()));
        Money m2 = debt.multiply(n2 * rate().value()).multiply(1.0d / 100.0d / (Year.from(d2).length()));

        final Money result = m1.add(m2);
        return payoffFee == null ? result : result.add(payoffFee);
    }

    @Override
    public Schedule schedule(List<Schedule> past, int i) {
        Schedule prev = past.isEmpty() ? null : past.get(past.size() - 1);
        Money debt = prev == null ? value() : prev.debt().subtract(prev.payoff());
        Money previousPayoff = prev == null ? null : prev.payoff();
        final LocalDate paymentDate = paymentDate(i);
        Money cost = calculatePercent(debt, previousPayoff, i);

        // Первый платеж - только проценты. Для других платежей считаем изначальную сумму.
        Money payment = i == 1 ? cost : calculatePayment(value(), duration());

        return new Schedule(
                i,
                paymentDate,
                nominalDate(i),
                duration() - i,
                debt,
                payment,
                i == 1 ? Money.kopecks(0) : payment.subtract(cost),
                cost
        );
    }
}
