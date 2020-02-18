// 이벤트가 발생하지 않으면 시한 만료시키기

Observable<Confirmation> confirmation() {
    Observable<Confirmation> delayBeforeCompletion =
	Observable
	    .<Confimation>empty()
	    .delay(200, MILLISECONDS);
    return Observable
	    .just(new Confirmation())
	    .delay(100, MILLISECONDS)
  	    .concatWith(delayBeforeCompletion);
}


import java.util.concurrent.TimeoutException;

//...

confirmation()
    .timeout(210, MILLISECONDS)
    .forEach(
	System.out::println,
	th -> {
	    if (th instanceof TimeoutException) {
		System.out.println("Too long");
	    } else {
		th.printStackTrace();
	    }
	}
    );


Observable<LocalDate> nextSolarEclipse(LocalDate after) {
    return Observable
	.just(
	    LocalDate.of(2016, MARCH, 9),
	    LocalDate.of(2016, SEPTEMBER, 1),
	    LocalDate.of(2017, FEBRUARY, 26),
	    LocalDate.of(2017, AUGUST, 21),
	    LocalDate.of(2018, FEBRUARY, 15),
	    LocalDate.of(2018, JULY, 13),
	    LocalDate.of(2018, AUGUST, 11),
	    LocalDate.of(2019, JANUARY, 6),
	    LocalDate.of(2019, JULY, 2),
	    LocalDate.of(2019, DECEMBER, 26))
	.skipWhile(date -> !date.isAfter(after))
	.zipWith(
	    Observable.interval(500, 50, MILLISECONDS),
	    (date, x) -> date);
}


nextSolarEclipse(LocalDate.of(2016, SEPTEMBER, 1))
    .timeout(
	() -> Observable.timer(1000, TimeUnit.MILLISECONDS),
	date -> Observable.timer(100, MILLISECONDS))


Observable<TimeInterval<LocalDate>> intervals =
        nextSolarEclipse(LocalDate.of(2016, JANUARY, 1))
                .timeInterval();


/*
TimeInterval [intervalInMilliseconds=533, value=2016-03-09]
TimeInterval [intervalInMilliseconds=49, value=2016-09-01]
TimeInterval [intervalInMilliseconds=50, value=2017-02-26]
TimeInterval [intervalInMilliseconds=50, value=2017-08-21]
TimeInterval [intervalInMilliseconds=50, value=2018-02-15]
TimeInterval [intervalInMilliseconds=50, value=2018-07-13]
TimeInterval [intervalInMilliseconds=50, value=2018-08-11]
TimeInterval [intervalInMilliseconds=50, value=2019-01-06]
TimeInterval [intervalInMilliseconds=51, value=2019-07-02]
TimeInterval [intervalInMilliseconds=49, value=2019-12-26]
*/
