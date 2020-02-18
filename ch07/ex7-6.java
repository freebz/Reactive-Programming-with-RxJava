// 실패 후 재시도

Observable<String> risky() {
    return Observable.fromCallable(() -> {
	if (Math.random() < 0.1) {
	    Thread.sleep((long) (Math.random() * 2000));
	    return "OK";
	} else {
	    throw new RuntimeException("Transient");
	}
    });
}


risky()
    .timeout(1, SECONDS)
    .doOnError(th -> log.warn("Will retry", th))
    .retry()
    .subscribe(log::info);


risky().cached().retry() // 깨진 구현


Observable
    .defer(() -> risky())
    .retry()
