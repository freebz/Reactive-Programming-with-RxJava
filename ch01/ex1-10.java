Observable.create(s -> {
    new Thread(() -> {
        s.onNext("one");
	s.onNext("two");
	s.onNext("three");
	s.onNext("four");
	s.onCompleted();
    }).start();
});
