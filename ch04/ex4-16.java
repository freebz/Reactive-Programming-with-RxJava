// observeOn()으로 선언적 동시성 처리하기

log("Starting");
final Observable<String> obs = simple();
log("Created");
obs
        .doOnNext(x -> log("Found 1: " + x))
        .observeOn(schedulerA)
        .doOnNext(x -> log("Found 2: " + x))
        .subscribe(
	        x -> log("Got 1: " + 1),
		Throwable::printStackTrace,
		() -> log("Completed")
        );
log("Exiting");


/*
23  | main  | Starting
136 | main  | Created
163 | main  | Subscribed
163 | main  | Found 1: A
163 | main  | Found 1: B
163 | main  | Exiting
163 | Shced-A-0 | Found 2: A
164 | Shced-A-0 | Got 1: A
164 | Shced-A-0 | Found 2: B
164 | Shced-A-0 | Got 1: B
164 | Shced-A-0 | Completed
*/


log("Starting");
final Observable<String> obs = simple();
log("Created");
obs
        .doOnNext(x -> log("Found 1: " + x))
        .observeOn(schedulerB)
        .doOnNext(x -> log("Found 2: " + x))
        .observeOn(schedulerC)
        .doOnNext(x -> log("Found 3: " + x))
        .subscribeOn(schedulerA)
        .subscribe(
	        x -> log("Got 1: " + x),
		Throwable::printStackTrace,
		() -> log("Completed")
	);
log("Exiting");


/*
21  | main  | Starting
98  | main  | Created
108 | main  | Exiting
129 | Sched-A-0 | Subscribed
129 | Sched-A-0 | Found 1: A
129 | Sched-A-0 | Found 1: B
130 | Sched-B-0 | Found 2: A
130 | Sched-B-0 | Found 2: B
130 | Sched-C-0 | Found 3: A
130 | Sched-C-0 | Got 1: A
130 | Sched-C-0 | Found 3: B
130 | Sched-C-0 | Got 1: B
130 | Sched-C-0 | Completed
*/


log("Starting");
Observable<String> obs = Observable.create(subscriber -> {
    log("Subscribed");
    subscriber.onNext("A");
    subscriber.onNext("B");
    subscriber.onNext("C");
    subscriber.onNext("D");
    subscriber.onCompleted();
});
log("Created");
obs
    .subscribeOn(schedulerA)
    .flatMap(record -> store(record).subscribeOn(schedulerB))
    .observeOn(schedulerC)
    .subscribe(
	    x -> log("Got: " + x),
	    Throwable::printStackTrace,
	    () -> log("Completed")
    );
log("Exiting");


Observable<UUID> store(String s) {
    return Observable.create(subscriber -> {
	log("Storing " + s);
	// 힘든 일
	subscriber.onNext(UUID.randomUUID());
	subscriber.onCompleted();
    });
}


/*
26   | main  | Starting
93   | main  | Created
121  | main  | Exiting

122  | Sched-A-0 | Subscribed
124  | Sched-B-0 | Storing A
124  | Sched-B-1 | Storing B
124  | Sched-B-2 | Storing C
124  | Sched-B-3 | Storing D

1136 | Sched-C-1 | Get: 44b8b999-e687-485f-b17a-a11f6a4bb9ce
1136 | Sched-C-1 | Get: 532ed720-eb35-4764-844e-690327ac4fe8
1136 | Sched-C-1 | Get: 13ddf253-c720-48fa-b248-473759a2c2a
1136 | Sched-C-1 | Get: 0eced01d-3fa7-45ec-96fb-572ff1e33587
1137 | Sched-C-1 | Completed
*/
