// delay()를 사용한 재시도와 재시도 횟수 제한

resky()
    .timeout(1, SECONDS)
    .retry(10)


risky()
    .timeout(1, SECONDS)
    .retry((attempt, e) ->
	attempt <= 10 && !(e instanceof TimeoutException))


risky()
    .timeout(1, SECONDS)
    .retryWhen(failures -> failures.delay(1, SECONDS))


.retryWhen(failures -> failures.take(10))


static final int ATTEMPTS = 11;

//...
.retryWhen(failures -> failures
        .zipWith(Observable.range(1, ATTEMPTS), (err, attempt) ->
	        attempt < ATTEMPTS ?
		Observable.timer(1, SECONDS) :
		Observable.error(err))
	.flatMap(x -> x)
)


.retryWhen(failures -> failures
    .zipWith(Observable.range(1, ATTEMPTS),
        this::handleRetryAttempt)
    .flatMap(x -> x)
)

//...

Observable<Long> handleRetryAttempt(Throwable err, int attempt) {
    switch (attempt) {
        case 1:
	    return Observable.just(42L);
        case ATTEMPTS:
	    return Observable.error(err);
        default:
	    long expDelay = (long) Math.pow(2, attempt - 2);
	    return Observable.timer(expDelay, SECONDS);
    }
}
