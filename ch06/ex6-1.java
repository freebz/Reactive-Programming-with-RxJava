// 흐름 제어

// 추가적인 샘플링과 스로틀링

long startTime = System.currentTimeMillis();
Observable
    .interval(7, TimeUnit.MILLISECONDS)
    .timestamp()
    .sample(1, TimeUnit.SECONDS)
    .map(ts -> ts.getTimestampMillis() - startTime + "ms: " + ts.getValue())
    .take(5)
    .subscribe(System.out::println);


/*
1088ms: 141
2089ms: 284
3090ms: 427
4084ms: 569
5085ms: 712
*/


Observable<String> names = Observable
    .just("Mary", "Patricia", "Linda",
	"Barbara",
	"Elizabeth", "Jennifer", "Maria", "Susan",
	"Margaret", "Dorothy");

Observable<Long> absoluteDelayMillis = Observable
    .just(0.1, 0.6, 0.9
	1.1,
	3.3, 3.4, 3.5, 3.6,
	4.4, 4.8)
    .map(d -> (long)(d * 1_000));

Observable<String> delayedNames = names
    .zipWith(absoluteDelayMillis,
	(n, d) -> Observable
	        .just(n)
	        .delay(d, MILLISECONDS))
    .flatMap(o -> o);


delayedNames
    .sample(1, SECONDS)
    .subscribe(System.out::println);


/*
Linda
Barbara
Susan
*/


static <T> Observable<T> delayedCompletion() {
    return Observable.<T>empty().delay(1, SECONDS);
}

//...

delayedNames
    .concatWith(delayedCompletion())
    .sample(1, SECONDS)
    .subscribe(System.out::println);


// 서로 동등하다:
obs.sample(1, SECONDS);
obs.sample(Observable.interval(1, SECOND));


Observable<String> names = Observable
    .just("Mary", "Patricia", "Linda",
	"Barbara",
	"Elizabeth", "Jennifer", "Maria", "Susan",
	"Margaret", "Dorothy");

Observable<Long> absoluteDelayMillis = Observable
    .just(0.1, 0.6, 0.9
	1.1,
	3.3, 3.4, 3.5, 3.6,
	4.4, 4.8)
    .map(d -> (long)(d * 1_000));

//...

delayedNames
    .throttleFirst(1, SECONDS)
    .subscribe(System.out::println);


/*
Mary
Barbara
Elizabeth
Margaret
*/
