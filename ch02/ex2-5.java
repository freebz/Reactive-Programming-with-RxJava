// Observable.create() 정복

private static void log(Object msg) {
    System.out.println(
	Thread.currentThread().getName() +
	": " + msg);
}

//...

log("Before");
Observable
    .range(5, 3)
    .subscribe(i -> {
        log(i);
    });
log("After");


/*
main: Before
main: 5
main: 6
main: 7
main: After
*/


Observable<Integer> ints = Observable
    .create(new Observable.OnSubscribe<Integer>() {
	@Override
	public void call(Subscriber<? super Integer> subscriber) {
	    log("Create");
	    subscriber.onNext(5);
	    subscriber.onNext(6);
	    subscriber.onNext(7);
	    subscriber.onCompleted();
	    log("Completed");
	}
    });

log("Starting");
ints.subscribe(i -> log("Element: " + i));
log("Exit");


/*
main: Starting
main: Create
main: Element: 5
main: Element: 6
main: Element: 7
main: Completed
main: Exit
*/


static <T> Observable<T> just(T x) {
    return Observable.create(subscriber -> {
	    subscriber.onNext(x);
	    subscriber.onCompleted();
	}
    );
}
