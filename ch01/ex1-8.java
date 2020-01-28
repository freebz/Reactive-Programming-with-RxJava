// 동기 방식 계산(예: 연산자)

Onbservable<Integer> o = Observable.create(s -> {
    s.onNext(1);
    s.onNext(2);
    s.onNext(3);
    s.onCompleted();
});
o.map(i -> "Number " + i)
 .subscribe(s -> System.out.println(s));
