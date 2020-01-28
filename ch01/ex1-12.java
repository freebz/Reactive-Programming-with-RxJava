Observable<String> a = Observable.create(s -> {
    new Thread(() -> {
	s.onNext("one");
	s.onNext("two");
	s.onCompleted();
    }).start();
});

Observable<String> b = Observable.create(s -> {
    new Thread(() -> {
        s.onNext("three");
	s.onNext("four");
	s.onCompleted();
    }).start();
});

// 동시에 a와 b를 구독하여 제3의 순차적인 스트림으로 병합한다.
Observable<String> c = Observable.merge(a, b);
