// flatMap() 이후 이벤트 순서

just(10L, 1L)
    .flatMap(x ->
         just(x).delay(x, TimeUnit.SECONDS))
    .subscribe(System.out::println);


Observable
        .just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
        .flatMap(this::loadRecordsFor);


Observalbe<String> loadRecordsFor(DayOfWeek dow) {
    switch(dow) {
    case SUNDAY:
	return Observable
	    .interval(90, MILLISECONDS)
	    .take(5)
	    .map(i -> "Sun-" + i);
    case MONDAY:
	return Observable
	    .interval(65, MILLISECONDS)
	    .take(5)
	    .map(i -> "Mon-" + i);
	//...
    }
}
