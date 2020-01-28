Observable.create(s -> {
  ... 비동기 구독과 데이터 방출 ...
})
.doOnNext(i -> System.out.println(Thread.currentThread()))
.filter(i->i%2==0)
.map(i -> "값 " + i + " 는 " + Thread.currentThread() + " 에서 처리된다")
.subscribe(s -> System.out.println("값 =>" + s));
System.out.println("값이 출력되기 전에 나온다");
