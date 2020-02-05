// amb() 연산자

Observable<String> stream(int initialDelay, int interval, String name) {
    return Observable
	.interval(initialDelay, interval, MILLISECONDS)
	.map(x -> name + x)
	.doOnSubscribe(() ->
	    log.info("Subscribe to " + name))
	.doOnUnsubscribe(() ->
	    log.info("Unsubscribe from " + name));
}

//...

Observable.amb(
        stream(100, 17, "S"),
        stream(200, 10, "F")
).subscribe(log::info);


stream(100, 17, "S")
        .ambWith(stream(200, 10, "F"))
        .subscribe(log::info);


/*
14:46:13.334: Subscribe to S
14:46:13.341: Subscribe to F
14:46:13.439: Unsubscribe from F
14:46:13.442: S0
14:46:13.456: S1
14:46:13.473: S2
14:46:13.490: S3
14:46:13.507: S4
14:46:13.525: S5
*/
