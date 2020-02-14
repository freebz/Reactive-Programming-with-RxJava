// Observable과 Single의 상호 변환

Single<String> single = Single.create(subscriber -> {
    System.out.println("Subscribing");
    subscriber.onSuccess("42");
});

Single<String> cacheSingle = single
        .toObservable()
        .cache()
        .toSingle();

cachedSingle.subscribe(System.out::println);
cachedSingle.subscribe(System.out::println);


Single<Integer> emptySingle =
        Observable.<Integer>empty().toSingle();
Single<Integer> doubleSingle =
        Observable.just(1, 2).toSingle();


Single<Integer> ignored = Single
        .just(1)
        .toObservable()
        .ignoreElements()    // 문제 원인
        .toSingle();
