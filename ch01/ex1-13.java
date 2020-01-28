// 느긋함과 조급함

Observable<T> someData = Observable.create(s -> {
    getDataFromServerWithCallback(args, data -> {
	s.onNext(data);
	s.onCompleted();
    });
})


someData.subscribe(s -> System.out.println(s));


someData.subscribe(s -> System.out.println("Subscriber 1: " + s));
someData.subscribe(s -> System.out.println("Subscriber 2: " + s));


someData
    .onErrorResumeNext(lazyFallback)
    .subscribe(s -> System.out.println(s));
