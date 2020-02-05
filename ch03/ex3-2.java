// map()을 사용한 1:1 변환

import rx.functions.Func1;

Observable<Status> tweets = //...
Observable<Date> dates = tweets.map(new Func1<Status, Date>() {
    @Override
    public Date call(Status status) {
	return status.getCreatedAt();
    }
});

Observable<Date> dates =
        tweets.map((Status status) -> status.getCreatedAt());

Observable<Date> dates =
        tweets.map((status) -> status.getCreatedAt());

Observable<Date> dates =
        tweets.map(Status::getCreatedAt);


Observable<Instant> instants = tweets
        .map(Status::getCreatedAt)
        .map((Date d) -> d.toInstant());


Observable
        .just(8, 9, 10)
        .filter(i -> i % 3 > 0)
        .map(i -> "#" + i * 10)
        .filter(s -> s.length() < 4);


Observable
        .just(8, 9, 10)
        .doOnNext(i -> System.out.println("A: " + i))
        .filter(i -> i % 3 > 0)
        .doOnNext(i -> System.out.println("B: " + i))
        .map(i -> "#" + i * 10)
        .doOnNext(s -> System.out.println("C: " + s))
        .filter(s -> s.length() < 4)
        .subscribe(s -> System.out.println("D: " + s));


/*
A: 8
B: 8
C: #80
D: #80
A: 9
A: 10
B: 10
C: #100
*/
