// 테스트와 디버깅

// 가상 시간

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.Month

class PlusMinusMonthSpec extends Sepcification {

    static final LocalDate START_DATE =
	LocalDate.of(2016, Month.JANUARY, 1)

    @Unroll
    def '#date +/- 1 month gives back the same date'() {
	expect:
	    date == date.plusMonths(1).minusMonths(1)
	where:
	    date << (0..365).collect {
	        day -> START_DATE.plusDays(day)
	    }
    }
}


/*
date == date.plusMonths(1).minusMonths(1)
|    |  |    |             |
|    |  |    2016-02-29    2016-01-29
|    |  2016-01-30
|    false
2016-01-30


date == date.plusMonths(1).minusMonths(1)
|    |  |    |             |
|    |  |    2016-02-29    2016-01-29
|    |  2016-01-31
|    false
2016-01-31


date == date.plusMonths(1).minusMonths(1)
|    |  |    |             |
|    |  |    2016-04-30    2016-03-30
|    |  2016-03-31
|    false
2016-0331

...
 */


public abstract class Clock {

    public static Clock system(ZoneId zone) { /* ... */ }

    public long millis() {
	return instant().toEpochMilli();
    }

    public abstract Instant instant();
}
