// delay() 연산자로 이벤트를 지연시키기

import java.util.concurrent.TimeUnit;

just(x, y, z).delay(1, TimeUnit.SECONDS);


Observable
    .timer(1, TimeUnit.SECONDS)
    .flatMap(i -> Observalbe.just(x, y, z))


import static rx.Observable.timer;
import static java.util.concurrent.TimeUnit.SECONDS;

Observable
    .just("Lorem", "ipsum", "dolor", "sit", "amet",
	  "consectetur", "adipiscing", "elit")
    .delay(word -> timer(word.length(), SECONDS))
    .subscribe(System.out::println);
TimeUnit.SECONDS.sleep(15);


Observable
    .just("Lorem", "ipsum", "dolor", "sit", "amet",
	  "consectetur", "adipiscing", "elit")
    .flatMap(word ->
        timer(word.length(), SECONDS).map(x -> word))
