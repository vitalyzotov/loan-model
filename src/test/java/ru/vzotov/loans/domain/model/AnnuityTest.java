package ru.vzotov.loans.domain.model;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.calendar.domain.model.WorkCalendar;
import ru.vzotov.calendar.domain.model.WorkCalendars;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnuityTest {

    private static final Logger log = LoggerFactory.getLogger(AnnuityTest.class);

    private static final S[] scheduleAlfa = {
            new S(LocalDate.parse("2019-07-01"), Money.rubles(2300000.0), Money.rubles(35510.11), Money.rubles(52000), 60),
            new S(LocalDate.parse("2019-07-29"), Money.rubles(1683510.11), Money.rubles(15484.60), Money.rubles(38000), 59),
            new S(LocalDate.parse("2019-08-29"), Money.rubles(1560994.71), Money.rubles(15896.06), Money.rubles(36000), 58),
            new S(LocalDate.parse("2019-09-30"), Money.rubles(1440890.77), Money.rubles(15146.33), Money.rubles(34000), 57),
            new S(LocalDate.parse("2019-10-29"), Money.rubles(1322037.10), Money.rubles(12594.12), Money.rubles(31000), 56),
            new S(LocalDate.parse("2019-11-29"), Money.rubles(1253631.22), Money.rubles(12766.09), Money.rubles(30000), 55),
            new S(LocalDate.parse("2019-12-30"), Money.rubles(1186397.31), Money.rubles(12081.43), Money.rubles(29000), 54),
            new S(LocalDate.parse("2020-01-29"), Money.rubles(1163478.74), Money.rubles(11435.56), Money.rubles(29000), 53),
            new S(LocalDate.parse("2020-03-02"), Money.rubles(1129914.30), Money.rubles(12215.11), Money.rubles(28000), 52),
            new S(LocalDate.parse("2020-03-30"), Money.rubles(1034129.41), Money.rubles(9485.74), Money.rubles(26000), 51),
            new S(LocalDate.parse("2020-04-29"), Money.rubles(537563.58), Money.rubles(5283.10), Money.rubles(13800), 50),
            new S(LocalDate.parse("2020-05-29"), Money.rubles(529046.68), Money.rubles(5199.40), Money.rubles(13800), 49),
    };

    private static final S[] scheduleGpb = {
            new S(LocalDate.parse("2019-12-30"), Money.rubles(3241600.00), Money.rubles(21891.90), Money.rubles(21891.90), 240),
            new S(LocalDate.parse("2020-01-28"), Money.rubles(3241600.00), Money.rubles(23343.94), Money.rubles(28132.00), 239),
            new S(LocalDate.parse("2020-02-28"), Money.rubles(3236811.94), Money.rubles(23303.28), Money.rubles(28132.00), 238),
            new S(LocalDate.parse("2020-04-06"), Money.rubles(3231983.22), Money.rubles(21767.32), Money.rubles(28132.00), 237),
            new S(LocalDate.parse("2020-04-28"), Money.rubles(3225618.54), Money.rubles(23235.99), Money.rubles(28132.00), 236),
            new S(LocalDate.parse("2020-05-28"), Money.rubles(3220722.53), Money.rubles(22439.46), Money.rubles(28132.00), 235),
            new S(LocalDate.parse("2020-06-29"), Money.rubles(3215029.99), Money.rubles(23146.46), Money.rubles(28132.00), 234),
            new S(LocalDate.parse("2020-07-28"), Money.rubles(3210044.45), Money.rubles(22366.22), Money.rubles(28132.00), 233),
    };

    @Test
    public void testAlfa() {
        WorkCalendar calendar = WorkCalendars.CALENDAR_2019
                .merge(WorkCalendars.CALENDAR_2020)
                .merge(WorkCalendars.CALENDAR_2021)
                .merge(WorkCalendars.CALENDAR_2022)
                .merge(WorkCalendars.CALENDAR_2023)
                .merge(WorkCalendars.CALENDAR_2024);

        Loan loan = new Loan(
                new AnnuityAlfa(
                        Money.rubles(2300000.00),
                        LocalDate.of(2019, 5, 15),
                        new AnnualPercentageRate(11.99),
                        60,
                        calendar,
                        29
                ),
                Arrays.asList(
                        new ExtraPayment(LocalDate.parse("2019-07-01"), Money.rubles(600000.00)),
                        new ExtraPayment(LocalDate.parse("2019-07-29"), Money.rubles(100000.00)),
                        new ExtraPayment(LocalDate.parse("2019-08-29"), Money.rubles(100000.00)),
                        new ExtraPayment(LocalDate.parse("2019-09-30"), Money.rubles(100000.00)),
                        new ExtraPayment(LocalDate.parse("2019-10-29"), Money.rubles(50000.00)),
                        new ExtraPayment(LocalDate.parse("2019-11-29"), Money.rubles(50000.00)),
                        new ExtraPayment(LocalDate.parse("2019-12-30"), Money.rubles(6000.00)),
                        new ExtraPayment(LocalDate.parse("2020-01-29"), Money.rubles(16000.00)),
                        new ExtraPayment(LocalDate.parse("2020-03-02"), Money.rubles(80000.00)),
                        new ExtraPayment(LocalDate.parse("2020-03-30"), Money.rubles(482423.00))
                        , new ExtraPayment(LocalDate.parse("2020-03-30"), Money.rubles(-2371.43)) // Альфабанк по сути начислил долг
                        , new ExtraPayment(LocalDate.parse("2020-06-29"), Money.rubles(50000.0))
                        , new ExtraPayment(LocalDate.parse("2020-07-29"), Money.rubles(50000.0 + 1400.0))
                        , new ExtraPayment(LocalDate.parse("2020-08-31"), Money.rubles(50000.0 + 1400.0 + 1400.0))
                        , new ExtraPayment(LocalDate.parse("2020-09-29"), Money.rubles(50000.0 + 1400.0 + 1400.0 + 1400.0))
                        , new ExtraPayment(LocalDate.parse("2020-10-29"), Money.rubles(50000.0 + 1400.0 + 1400.0 + 1400.0 + 1600.0))
                ));

        log.info("Loan dates {}", loan.dates());

        List<Schedule> schedule = loan.schedule();


        int i = 0;
        for (S s : scheduleAlfa) {
            final Schedule fact = schedule.get(i);
            final Money fee = fact.cost();
            final Money payment = fact.payment();

            log.info("{}/{}: {}, debt = {}, fee = {} ({}) [{}], payment = {} ({})", i + 1, s.n, s.date, fact.debt(), fee, s.fee, s.fee.subtract(fee), payment, s.payment);

            assertThat(fee).isEqualTo(s.fee);
            assertThat(payment).isEqualTo(s.payment);

            i++;
        }

        for (Schedule s : schedule) {
            log.info("| {}/{}: {}, debt = {}, cost = {}, payment = {}, total = {}", s.period(), s.remainingPeriods(), s.date(), s.debt(), s.cost(), s.payment(), s.cost().add(s.payoff()));
        }
    }

    @Test
    public void testGpb() {
        WorkCalendar calendar = WorkCalendars.CALENDAR_2019
                .merge(WorkCalendars.CALENDAR_2020)
                .merge(WorkCalendars.CALENDAR_2020_COVID19)
                .merge(WorkCalendars.CALENDAR_2021)
                .merge(WorkCalendars.CALENDAR_2022)
                .merge(WorkCalendars.CALENDAR_2023)
                .merge(WorkCalendars.CALENDAR_2024)
                .merge(WorkCalendars.CALENDAR_2025)
                .merge(WorkCalendars.CALENDAR_2026)
                .merge(WorkCalendars.CALENDAR_2027)
                .merge(WorkCalendars.CALENDAR_2028)
                .merge(WorkCalendars.CALENDAR_2029)
                .merge(WorkCalendars.CALENDAR_2030)
                .merge(WorkCalendars.CALENDAR_2031)
                .merge(WorkCalendars.CALENDAR_2032)
                .merge(WorkCalendars.CALENDAR_2033)
                .merge(WorkCalendars.CALENDAR_2034)
                .merge(WorkCalendars.CALENDAR_2035)
                .merge(WorkCalendars.CALENDAR_2036)
                .merge(WorkCalendars.CALENDAR_2037)
                .merge(WorkCalendars.CALENDAR_2038)
                .merge(WorkCalendars.CALENDAR_2039);


        Loan loan = new Loan(
                new AnnuityGpb(
                        Money.rubles(3241600.00),
                        LocalDate.of(2019, 11, 29),
                        new AnnualPercentageRate(8.50),
                        240,
                        calendar,
                        28
                ),
                Collections.emptyList());

        log.info("Loan dates {}", loan.dates());

        List<Schedule> schedule = loan.schedule();

        int i = 0; // порядковый номер платежа
        log.info("GPB {}", loan.date());
        for (S s : scheduleGpb) {
            final Schedule fact = schedule.get(i);
            final Money fee = fact.cost();
            final Money payment = fact.payment();

            log.info("{}/{}: {}, debt = {}, fee = {} ({}) [{}], payment = {} ({})", i + 1, s.n, s.date, fact.debt(), fee, s.fee, s.fee.subtract(fee), payment, s.payment);
            assertThat(fee).isEqualTo(s.fee);
            assertThat(payment).isEqualTo(s.payment);
            i++;
        }

    }

    private record S(LocalDate date, Money debt, Money fee, Money payment, int n) {
    }
}
