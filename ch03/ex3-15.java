// single()을 사용하여 Observable이 정확히 하나만 갖는다고 단언하기

// distinct()와 distinctUntilChange()로 중복 제거하기

Observable<Integer> randomInts = Observable.create(subscriber -> {
    Random random = new Random();
    while (!subscriber.isUnsubscribed()) {
	subscriber.onNext(random.nextInt(1000));
    }
});


Observable<Integer> uniqueRandomInts = randomInts
        .distinct()
        .take(10);


Observable<Status> tweets = //...

Observable<Long> distinctUserIds = tweets
        .map(status -> status.getUser().getId())
        .distinct();


Observable<Status> distinctUserIds = tweets
        .distinct(status -> status.getUser().getId());


Observable<Weather> measurements = //...

Observable<Weather> tempChanges = measurements
        .distinctUntilChange(Weather::getTemperature);
