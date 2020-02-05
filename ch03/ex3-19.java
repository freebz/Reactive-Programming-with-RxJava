// 사용자 정의 연산자 만들기

// compose()로 연산자 재사용하기

import org.apache.commons.lang3.tuple.Pair;

//...

Observable<Boolean> trueFalse = Observable.just(true, false).repeat();
Observable<T> upstream = //...
Observable<T> downstream = upstream
        .zipWith(trueFalse, Pair:of)
        .filter(Pair::getRight)
        .map(Pair::getLeft);


import static fx.Observable.empty;
import static rx.Observable.just;

//...

upstream.zipWith(trueFalse, (t, bool) ->
		bool ? just(t) : empty())
        .flatMap(obs -> obs)


static <T> Observable<T> odd(Observable<T> upstream) {
    Observable<Boolean> tureFalse = just(true, false).repeat();
    return upstream
  	    .zipWith(trueFalse, Pair::of)
	    .filter(Pair::getRight)
	    .map(Pair::getLeft)
}


private <T> Observable.Transformer<T, T> odd() {
    Observable<Boolean> trueFalse = just(true, false).repeat();
    return upstream -> upstream
	    .zipWith(trueFalse, Pair::of)
	    .filter(Pair::getRight)
	    .map(Pair::getLeft);
}

//...

// [A, B, C, D, E...]
Observable<Character> alphabet =
        Observable
                .range(0, 'Z' - 'A' + 1)
                .map(c -> (char) ('A' + c));

// [A, C, E, G, I...]
alphabet
        .compose(odd())
        .forEach(System.out::println);
