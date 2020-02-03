// 여러 구독자 관리하기

Observable<Integer> ints =
    Observable.create(subscriber -> {
	    log("Create");
	    subscriber.onNext(42);
	    subscriber.onCompleted();
	}
    );
log("Starting");
ints.subscribe(i -> log("Element A: " + i));
ints.subscribe(i -> log("Element B: " + i));
log("Exit");


/*
main: Starting
main: Create
main: Element A: 42
main: Create
main: Element B: 42
main: Exit
*/


Observable<Integer> ints =
    Observable.<Integer>create(subscriber -> {
	    //...
	}
    )
    .cache();


/*
main: Starting
main: Create
main: Element A: 42
main: Element B: 42
main: Exit
*/
