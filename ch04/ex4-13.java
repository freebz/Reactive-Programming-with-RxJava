// subscribeOn()을 사용한 선언적 구독

Observable<String> simple() {
    return Observable.create(subscriber -> {
	log("Subscribed");
	subscriber.onNext("A");
	subscriber.onNext("B");
	subscriber.onCompleted();
    });
}

//...

log("Starting");
final Observable<String> obs = simple();
log("Created");
final Observable<String> obs2 = obs
        .map(x -> x)
        .filter(x -> true);
log("Transformed");
obs2.subscribe(
        x -> log("Got " + x),
        Throwable::printStackTrace,
        () -> log("Completed")
);
log("Exiting");


/*
33  | main  | Starting
120 | main  | Created
128 | main  | Transformed
133 | main  | Subscribed
133 | main  | Got A
133 | main  | Got B
133 | main  | Completed
134 | main  | Exiting
*/


log("Starting");
final Observable<String> obs = simple();
log("Created");
obs
        .subscribeOn(schedulerA)
        .subscribe(
	        x -> log("Got " + x),
		Throwable::printStackTrace,
		() -> log("Completed")
        );
log("Exiting");

/*
35  | main  | Starting
112 | main  | Created
123 | main  | Exiting
123 | Sched-A-0 | Subscribed
124 | Sched-A-0 | Got A
124 | Sched-A-0 | Got B
124 | Sched-A-0 | Completed
*/


import static java.uitl.concurrent.Executors.newFixedThreadPool;

ExecutorService poolA = newFixedThreadPool(10, threadFactory("Sched-A-%d"));
Scheduler schedulerA = Schedulers.from(poolA);

ExecutorService poolB = newFixedThreadPool(10, threadFactory("Sched-B-%d"));
Scheduler schedulerB = Schedulers.from(poolB);

ExecutorService poolC = newFixedThreadPool(10, threadFactory("Sched-C-%d"));
Scheduler schedulerC = Schedulers.from(poolC);

private ThreadFactory threadFactory(String pattern) {
    return new ThreadFactoryBuild()
	.setNameFormat(pattern)
	.build();
}


// 이렇게 하지 말자
Observable<String> obs = Observable.create(subscriber -> {
    log("Subscribed");
    Runnable code = () -> {
	subscriber.onNext("A");
	subscriber.onNext("B");
	subscriber.onCompleted();
    };
    new Thread(code, "Async").start();
});
