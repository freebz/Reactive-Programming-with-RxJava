// debounce()에서 리소스 고갈 회피하기

Observable
    .interval(99, MILLISECONDS)
    .debounce(100, MILLISECONDS)


Observable
        .interval(99, MILLISECONDS)
        .debounce(100, MILLISECONDS)
        .timeout(1, SECONDS);


ConnectableObservable<Long> upstream = Observable
        .interval(99, MILLISECONDS)
        .publish();
upstream
        .debounce(100, MILLISECONDS)
        .timeout(1, SECONDS, upstream.take(1));
upstream.connect();


upstream
    .debounce(100, MILLISECONDS)
    .timeout(1, SECONDS, upstream
        .take(1)
	.concatWith(
	    upstream.debounce(100, MILLISECONDS)))


upstream
    .debounce(100, MILLISECONDS)
    .timeout(1, SECONDS, upstream
	.take(1)
        .concatWith(
	    upstream
	        .debounce(100, MILLISECONDS)
 	        .timeout(1, SECONDS, upstream)))


import static rx.Observable.defer;

Observable<Long> timedDebounce(Observable<Long> upstream) {
    Observable<Long> onTimeout = upstream
	    .take(1)
 	    .concatWith(defer(() -> timedDebounce(upstream)));
    return upstream
	    .debounce(100, MILLISECONDS)
   	    .timeout(1, SECONDS, onTimeout);
}
