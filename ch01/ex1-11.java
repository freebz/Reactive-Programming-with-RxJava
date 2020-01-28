// 이렇게 하지 말 것
Observable.create(s -> {
    // 스레드 A
    new Thread(() -> {
	s.onNext("one");
	s.onNext("two");
    }).start();

    // 스레드 B
    new Thread(() -> {
	s.onNext("three");
	s.onNext("four");
    }).start();

    // 스레드 경합 문제로 s.onCompleted() 호출을 생략해야 한다
});
// 이렇게 하지 말 것
