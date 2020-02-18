// 오류 처리

// 예외는 어디에?

Observable
    .create(subscriber -> {
	try {
	    subscriber.onNext(1 / 0);
	} catch (Exception e) {
	    subscriber.onError(e);
	}
    })
    // 잘못된 구현: onError() 콜백이 없다
    .subscribe(System.out::println);


private static final Logger log = LoggerFactory.getLogger(My.class);

//...

.subscribe(
        System.out::println,
	throwable -> log.error("That escalated quickly", throwable));


Observable.create(subscriber -> {
    try {
	subscriber.onNext(1/ 0);
    } catch (Exception e) {
	subscriber.onError(e);
    }
});


Observable.create(subscriber -> subscriber.onNext(1 / 0));


Observable.fromCallable(() -> 1 / 0);


Observable
    .just(1, 0)
    .map(x -> 10 / x);

Observable
    .just("Lorem", null, "ipsum")
    .filter(String::isEmpty);


Observable
        .just(1, 0)
        .flatMap(x -> (x == 0) ?
		Observable.error(new ArithmeticException("Zero :-(")) :
		Observable.just(10 / x)
);
