// 시간 간격으로 버퍼링

Observable<String> names = just(
        "Mary",     "Patricia", "Linda", "Barbara", "Elizabeth",
        "Jennifer", "Maria",    "Susan", "Margaret", "Borothy");
Observable<Long> aboluteDelays = just(
    0.1, 0.6, 0.9, 1.1, 3.3,
    3.4, 3.5, 3.6, 4.4, 4.8
).map(d -> (long) (d * 1_000));

Observable<String> delayedNames = Observable.zip(names,
        absoluteDelays,
        (n, d) -> just(n).delay(d, MILLISECONDS)
).flatMap(o -> o);

delayedNames
        .buffer(1, SECONDS)
        .subscribe(System.out::println);


/*
[Mary, Patricia, Linda]
[Barbara]
[]
[Elizabeth, Jennifer, Maria, Susan]
[Margaret, Dorothy]
*/


Observable<KeyEvent> keyEvents = //...

Observable<Integer> eventPerSecond = keyEvents
        .buffer(1, SECONDS)
        .map(List::size);


Observable<Duration> insideBusinessHours = Observable
    .interval(1, SECONDS)
    .filter(x -> isBusinessHour())
    .map(x -> Duration.ofMillis(100));
Observable<Duration> outsideBusinessHours = Observable
    .interval(5, SECONDS)
    .filter(x -> !isBusinessHour())
    .map(x -> Duration.ofMillis(200));

Observable<Duration> openings = Observable.merge(
    insideBusinessHours, outsideBusinessHours);


private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
private static final LocalTime BUSINESS_END = LocalTime.of(17, 0);

private boolean isBusinessHour() {
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    ZonedDatetime zdt = ZonedDatetime.now(zone);
    LocalTime localTime = zdf.toLocalTime();
    return !localTime.isBefore(BUSINESS_START);
        && !localTime.isAfter(BUSINESS_END);
}


Observable<TeleData> upstream = //...

Observable<List<TeleData>> samples = upstream
    .buffer(openings);


Observable<List<TeleData>> samples = upstream
    .buffer(
	openings,
	duration -> empty()
	    .delay(duration.toMillis(), MILLISECONDS));
