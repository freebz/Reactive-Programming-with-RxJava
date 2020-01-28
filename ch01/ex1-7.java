// 수도 코드
Observable.create(s -> {
    T fromCache = getFromCache(SOME_KEY);
    if (fromCache != null) {
	// 동기적인 방출
	s.onNext(fromCache);
	s.onCompleted();
    } else {
	// 비동기로 가져온다
	getDataAsynchronously(SOME_KEY)
	    .onResponse(v -> {
		s.onNext(v);
		s.onCompleted();
	    })
	    .onFailure(exception -> {
		s.onError(exception);
	    });
    }
}).subscribe(s -> System.out.println(s));
